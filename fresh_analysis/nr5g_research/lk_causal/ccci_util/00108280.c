
undefined1 get_capability_bit(char *param_1)

{
  undefined1 uVar1;
  int iVar2;
  
  uVar1 = 0;
  if (param_1 != (char *)0x0) {
    iVar2 = strcmp(param_1,"N");
    if ((iVar2 == 0) || (iVar2 = strcmp(param_1,"n"), iVar2 == 0)) {
      uVar1 = 0x40;
    }
    else {
      iVar2 = strcmp(param_1,"LF");
      if (((iVar2 == 0) || (iVar2 = strcmp(param_1,"Lf"), iVar2 == 0)) ||
         (iVar2 = strcmp(param_1,"lf"), iVar2 == 0)) {
        uVar1 = 0x20;
      }
      else {
        iVar2 = strcmp(param_1,"LT");
        if (((iVar2 == 0) || (iVar2 = strcmp(param_1,"Lt"), iVar2 == 0)) ||
           (iVar2 = strcmp(param_1,"lt"), iVar2 == 0)) {
          uVar1 = 0x10;
        }
        else {
          iVar2 = strcmp(param_1,"W");
          if ((iVar2 == 0) || (iVar2 = strcmp(param_1,"w"), iVar2 == 0)) {
            uVar1 = 8;
          }
          else {
            iVar2 = strcmp(param_1,"C");
            if ((iVar2 == 0) || (iVar2 = strcmp(param_1,"c"), iVar2 == 0)) {
              uVar1 = 4;
            }
            else {
              iVar2 = strcmp(param_1,"T");
              if ((iVar2 == 0) || (iVar2 = strcmp(param_1,"t"), iVar2 == 0)) {
                uVar1 = 2;
              }
              else {
                iVar2 = strcmp(param_1,"G");
                if (iVar2 == 0) {
                  uVar1 = 1;
                }
                else {
                  iVar2 = strcmp(param_1,"g");
                  uVar1 = iVar2 == 0;
                }
              }
            }
          }
        }
      }
    }
  }
  return uVar1;
}

