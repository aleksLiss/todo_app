function debounce(func, wait) {
  let timeout;
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout);
      func(...args);
    };
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
  };
}

function loadUserData() {
  const token = localStorage.getItem('jwt');
  if (!token || token === 'null' || token === 'undefined') {
    $('main .container').html('<p class="main__welcome">Welcome! Please register or log in.</p>');
    return;
  }

  $.ajax({
    url: `/api/user`,
    type: 'GET',
    beforeSend: function(xhr) {
      const authValue = token.startsWith('Bearer ') ? token : 'Bearer ' + token;
      xhr.setRequestHeader('Authorization', authValue);
    },
    headers: {
      'Authorization': token.startsWith('Bearer ') ? token : 'Bearer ' + token
    },
    success: (data) => {
      const navHtml = `
                <span class="header__user">${data.email}</span>
                <button class="btn btn--error" id="logout-btn">Logout</button>
            `;
      $('header .header__nav').html(navHtml);
      $('main .container').html(`
                <div class="add-task-container">
                    <button class="add-task-btn" id="add-task-btn">Add New Task</button>
                </div>
                <div class="tasks">
                    <div class="tasks__column tasks__column--todo">
                        <h2 class="tasks__title">To Do<span class="tasks__count"></span></h2>
                        <div class="tasks__list" id="todo-list"></div>
                    </div>
                    <div class="tasks__column tasks__column--done">
                        <h2 class="tasks__title">Completed<span class="tasks__count"></span></h2>
                        <div class="tasks__list" id="done-list"></div>
                    </div>
                </div>
            `);

      $('#logout-btn').click(() => {
        localStorage.removeItem('jwt');
        location.reload();
      });
      loadTasks();
      setupEventListeners();
    },
    error: (xhr) => {
      if (xhr.status === 401) {
        localStorage.removeItem('jwt');
        showNotification('Session expired, please log in again.', 'error', () => {
          location.reload();
        });
      }
    }
  });
}