
int FUN_ffff0000be0a09b8(long param_1,long param_2,long param_3)

{
  long lVar1;
  long lVar2;
  int iVar3;
  undefined8 local_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 local_70;
  undefined8 local_68;
  undefined8 uStack_60;
  undefined8 local_58;
  undefined8 local_50;
  undefined8 uStack_48;
  
  local_50 = 0;
  uStack_48 = 0;
  local_58 = 0;
  local_68 = 0;
  uStack_60 = 0;
  local_78 = 0;
  local_70 = 0;
  local_88 = 0;
  uStack_80 = 0;
  local_98 = 0;
  uStack_90 = 0;
  local_a0 = 0;
  FUN_ffff0000be09bcf0(&local_58);
  FUN_ffff0000be09bcf0(&local_70);
  FUN_ffff0000be09bcf0(&local_88);
  FUN_ffff0000be09bcf0(&local_a0);
  if (*(long *)(param_1 + 0x30) == 0) {
    iVar3 = FUN_ffff0000be09d11c(&local_70,param_3 + 0x30,param_3 + 0x30);
    if (iVar3 == 0) {
      iVar3 = FUN_ffff0000be0a1374(&local_70,param_1);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000be09cef8(&local_88,param_3,&local_70);
        if (iVar3 == 0) {
          lVar1 = param_1 + 8;
          do {
            iVar3 = FUN_ffff0000be09c8f4(&local_88,lVar1);
            if (iVar3 < 0) {
              iVar3 = FUN_ffff0000be09d008(&local_a0,param_3,&local_70);
              if (iVar3 == 0) goto LAB_ffff0000be0a0c00;
              break;
            }
            iVar3 = FUN_ffff0000be09cd08(&local_88,&local_88,lVar1);
          } while (iVar3 == 0);
        }
      }
    }
  }
  else {
    iVar3 = FUN_ffff0000be09d11c(&local_70,param_3,param_3);
    if (iVar3 == 0) {
      iVar3 = FUN_ffff0000be0a1374(&local_70,param_1);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000be09c364(&local_58,&local_70,3);
        if (iVar3 == 0) {
          lVar1 = param_1 + 8;
          do {
            iVar3 = FUN_ffff0000be09c8f4(&local_58,lVar1);
            if (iVar3 < 0) {
              iVar3 = FUN_ffff0000be09c9e4(param_1 + 0x20,0);
              if (iVar3 == 0) goto LAB_ffff0000be0a0ca4;
              iVar3 = FUN_ffff0000be09d11c(&local_70,param_3 + 0x30,param_3 + 0x30);
              if (iVar3 == 0) {
                iVar3 = FUN_ffff0000be0a1374(&local_70,param_1);
                if (iVar3 == 0) {
                  iVar3 = FUN_ffff0000be09d11c(&local_88,&local_70,&local_70);
                  if (iVar3 == 0) {
                    iVar3 = FUN_ffff0000be0a1374(&local_88,param_1);
                    if (iVar3 == 0) {
                      iVar3 = FUN_ffff0000be09d11c(&local_70,&local_88,param_1 + 0x20);
                      if (iVar3 == 0) {
                        iVar3 = FUN_ffff0000be0a1374(&local_70,param_1);
                        if (iVar3 == 0) {
                          iVar3 = FUN_ffff0000be09cef8(&local_58,&local_58,&local_70);
                          if (iVar3 == 0) goto LAB_ffff0000be0a0bbc;
                        }
                      }
                    }
                  }
                }
              }
              break;
            }
            iVar3 = FUN_ffff0000be09cd08(&local_58,&local_58,lVar1);
          } while (iVar3 == 0);
        }
      }
    }
  }
  goto LAB_ffff0000be0a0d44;
  while( true ) {
    iVar3 = FUN_ffff0000be09cef8(&local_a0,&local_a0,lVar1);
    if (iVar3 != 0) break;
LAB_ffff0000be0a0c00:
    if ((-1 < (short)local_a0) || (iVar3 = FUN_ffff0000be09c9e4(&local_a0,0), iVar3 == 0)) {
      iVar3 = FUN_ffff0000be09d11c(&local_70,&local_88,&local_a0);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000be0a1374(&local_70,param_1);
        if (iVar3 == 0) {
          iVar3 = FUN_ffff0000be09c364(&local_58,&local_70,3);
          if (iVar3 == 0) goto LAB_ffff0000be0a0c78;
        }
      }
      break;
    }
  }
  goto LAB_ffff0000be0a0d44;
  while( true ) {
    iVar3 = FUN_ffff0000be09cd08(&local_58,&local_58,lVar1);
    if (iVar3 != 0) break;
LAB_ffff0000be0a0c78:
    iVar3 = FUN_ffff0000be09c8f4(&local_58,lVar1);
    if (iVar3 < 0) goto LAB_ffff0000be0a0ca4;
  }
  goto LAB_ffff0000be0a0d44;
  while( true ) {
    iVar3 = FUN_ffff0000be09cd08(&local_58,&local_58,lVar1);
    if (iVar3 != 0) break;
LAB_ffff0000be0a0bbc:
    iVar3 = FUN_ffff0000be09c8f4(&local_58,lVar1);
    if (iVar3 < 0) goto LAB_ffff0000be0a0ca4;
  }
  goto LAB_ffff0000be0a0d44;
LAB_ffff0000be0a0ca4:
  lVar1 = param_3 + 0x18;
  iVar3 = FUN_ffff0000be09d11c(&local_88,lVar1,lVar1);
  if (iVar3 == 0) {
    iVar3 = FUN_ffff0000be0a1374(&local_88,param_1);
    if (iVar3 == 0) {
      iVar3 = FUN_ffff0000be09c52c(&local_88,1);
      if (iVar3 == 0) {
        lVar2 = param_1 + 8;
        do {
          iVar3 = FUN_ffff0000be09c8f4(&local_88,lVar2);
          if (iVar3 < 0) {
            iVar3 = FUN_ffff0000be09d11c(&local_70,param_3,&local_88);
            if (iVar3 == 0) {
              iVar3 = FUN_ffff0000be0a1374(&local_70,param_1);
              if (iVar3 == 0) {
                iVar3 = FUN_ffff0000be09c52c(&local_70,1);
                if (iVar3 == 0) goto LAB_ffff0000be0a0d98;
              }
            }
            break;
          }
          iVar3 = FUN_ffff0000be09cd08(&local_88,&local_88,lVar2);
        } while (iVar3 == 0);
      }
    }
  }
  goto LAB_ffff0000be0a0d44;
  while( true ) {
    iVar3 = FUN_ffff0000be09cd08(&local_70,&local_70,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000be0a0d98:
    iVar3 = FUN_ffff0000be09c8f4(&local_70,lVar2);
    if (iVar3 < 0) {
      iVar3 = FUN_ffff0000be09d11c(&local_a0,&local_88,&local_88);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000be0a1374(&local_a0,param_1);
        if (iVar3 == 0) {
          iVar3 = FUN_ffff0000be09c52c(&local_a0,1);
          if (iVar3 == 0) goto LAB_ffff0000be0a0e04;
        }
      }
      break;
    }
  }
  goto LAB_ffff0000be0a0d44;
  while( true ) {
    iVar3 = FUN_ffff0000be09cd08(&local_a0,&local_a0,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000be0a0e04:
    iVar3 = FUN_ffff0000be09c8f4(&local_a0,lVar2);
    if (iVar3 < 0) {
      iVar3 = FUN_ffff0000be09d11c(&local_88,&local_58,&local_58);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000be0a1374(&local_88,param_1);
        if (iVar3 == 0) {
          iVar3 = FUN_ffff0000be09d008(&local_88,&local_88,&local_70);
          if (iVar3 == 0) goto LAB_ffff0000be0a0e74;
        }
      }
      break;
    }
  }
  goto LAB_ffff0000be0a0d44;
  while( true ) {
    iVar3 = FUN_ffff0000be09cef8(&local_88,&local_88,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000be0a0e74:
    if ((-1 < (short)local_88) || (iVar3 = FUN_ffff0000be09c9e4(&local_88,0), iVar3 == 0)) {
      iVar3 = FUN_ffff0000be09d008(&local_88,&local_88,&local_70);
      if (iVar3 == 0) goto LAB_ffff0000be0a0ec0;
      break;
    }
  }
  goto LAB_ffff0000be0a0d44;
  while( true ) {
    iVar3 = FUN_ffff0000be09cef8(&local_88,&local_88,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000be0a0ec0:
    if ((-1 < (short)local_88) || (iVar3 = FUN_ffff0000be09c9e4(&local_88,0), iVar3 == 0)) {
      iVar3 = FUN_ffff0000be09d008(&local_70,&local_70,&local_88);
      if (iVar3 == 0) goto LAB_ffff0000be0a0f0c;
      break;
    }
  }
  goto LAB_ffff0000be0a0d44;
  while( true ) {
    iVar3 = FUN_ffff0000be09cef8(&local_70,&local_70,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000be0a0f0c:
    if ((-1 < (short)local_70) || (iVar3 = FUN_ffff0000be09c9e4(&local_70,0), iVar3 == 0)) {
      iVar3 = FUN_ffff0000be09d11c(&local_70,&local_70,&local_58);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000be0a1374(&local_70,param_1);
        if (iVar3 == 0) {
          iVar3 = FUN_ffff0000be09d008(&local_70,&local_70,&local_a0);
          if (iVar3 == 0) goto LAB_ffff0000be0a0f84;
        }
      }
      break;
    }
  }
  goto LAB_ffff0000be0a0d44;
  while( true ) {
    iVar3 = FUN_ffff0000be09cef8(&local_70,&local_70,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000be0a0f84:
    if ((-1 < (short)local_70) || (iVar3 = FUN_ffff0000be09c9e4(&local_70,0), iVar3 == 0)) {
      iVar3 = FUN_ffff0000be09d11c(&local_a0,lVar1,param_3 + 0x30);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000be0a1374(&local_a0,param_1);
        if (iVar3 == 0) {
          iVar3 = FUN_ffff0000be09c52c(&local_a0,1);
          if (iVar3 == 0) goto LAB_ffff0000be0a0ff8;
        }
      }
      break;
    }
  }
  goto LAB_ffff0000be0a0d44;
  while( true ) {
    iVar3 = FUN_ffff0000be09cd08(&local_a0,&local_a0,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000be0a0ff8:
    iVar3 = FUN_ffff0000be09c8f4(&local_a0,lVar2);
    if (iVar3 < 0) {
      iVar3 = FUN_ffff0000be09bf10(param_2,&local_88);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000be09bf10(param_2 + 0x18,&local_70);
        if (iVar3 == 0) {
          iVar3 = FUN_ffff0000be09bf10(param_2 + 0x30,&local_a0);
        }
      }
      break;
    }
  }
LAB_ffff0000be0a0d44:
  FUN_ffff0000be09bd08(&local_58);
  FUN_ffff0000be09bd08(&local_70);
  FUN_ffff0000be09bd08(&local_88);
  FUN_ffff0000be09bd08(&local_a0);
  return iVar3;
}


