
int FUN_ffff0000907cbd08(long param_1,long param_2,long param_3)

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
  FUN_ffff0000907c7040(&local_58);
  FUN_ffff0000907c7040(&local_70);
  FUN_ffff0000907c7040(&local_88);
  FUN_ffff0000907c7040(&local_a0);
  if (*(long *)(param_1 + 0x30) == 0) {
    iVar3 = FUN_ffff0000907c846c(&local_70,param_3 + 0x30,param_3 + 0x30);
    if (iVar3 == 0) {
      iVar3 = FUN_ffff0000907cc6c4(&local_70,param_1);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000907c8248(&local_88,param_3,&local_70);
        if (iVar3 == 0) {
          lVar1 = param_1 + 8;
          do {
            iVar3 = FUN_ffff0000907c7c44(&local_88,lVar1);
            if (iVar3 < 0) {
              iVar3 = FUN_ffff0000907c8358(&local_a0,param_3,&local_70);
              if (iVar3 == 0) goto LAB_ffff0000907cbf50;
              break;
            }
            iVar3 = FUN_ffff0000907c8058(&local_88,&local_88,lVar1);
          } while (iVar3 == 0);
        }
      }
    }
  }
  else {
    iVar3 = FUN_ffff0000907c846c(&local_70,param_3,param_3);
    if (iVar3 == 0) {
      iVar3 = FUN_ffff0000907cc6c4(&local_70,param_1);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000907c76b4(&local_58,&local_70,3);
        if (iVar3 == 0) {
          lVar1 = param_1 + 8;
          do {
            iVar3 = FUN_ffff0000907c7c44(&local_58,lVar1);
            if (iVar3 < 0) {
              iVar3 = FUN_ffff0000907c7d34(param_1 + 0x20,0);
              if (iVar3 == 0) goto LAB_ffff0000907cbff4;
              iVar3 = FUN_ffff0000907c846c(&local_70,param_3 + 0x30,param_3 + 0x30);
              if (iVar3 == 0) {
                iVar3 = FUN_ffff0000907cc6c4(&local_70,param_1);
                if (iVar3 == 0) {
                  iVar3 = FUN_ffff0000907c846c(&local_88,&local_70,&local_70);
                  if (iVar3 == 0) {
                    iVar3 = FUN_ffff0000907cc6c4(&local_88,param_1);
                    if (iVar3 == 0) {
                      iVar3 = FUN_ffff0000907c846c(&local_70,&local_88,param_1 + 0x20);
                      if (iVar3 == 0) {
                        iVar3 = FUN_ffff0000907cc6c4(&local_70,param_1);
                        if (iVar3 == 0) {
                          iVar3 = FUN_ffff0000907c8248(&local_58,&local_58,&local_70);
                          if (iVar3 == 0) goto LAB_ffff0000907cbf0c;
                        }
                      }
                    }
                  }
                }
              }
              break;
            }
            iVar3 = FUN_ffff0000907c8058(&local_58,&local_58,lVar1);
          } while (iVar3 == 0);
        }
      }
    }
  }
  goto LAB_ffff0000907cc094;
  while( true ) {
    iVar3 = FUN_ffff0000907c8248(&local_a0,&local_a0,lVar1);
    if (iVar3 != 0) break;
LAB_ffff0000907cbf50:
    if ((-1 < (short)local_a0) || (iVar3 = FUN_ffff0000907c7d34(&local_a0,0), iVar3 == 0)) {
      iVar3 = FUN_ffff0000907c846c(&local_70,&local_88,&local_a0);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000907cc6c4(&local_70,param_1);
        if (iVar3 == 0) {
          iVar3 = FUN_ffff0000907c76b4(&local_58,&local_70,3);
          if (iVar3 == 0) goto LAB_ffff0000907cbfc8;
        }
      }
      break;
    }
  }
  goto LAB_ffff0000907cc094;
  while( true ) {
    iVar3 = FUN_ffff0000907c8058(&local_58,&local_58,lVar1);
    if (iVar3 != 0) break;
LAB_ffff0000907cbfc8:
    iVar3 = FUN_ffff0000907c7c44(&local_58,lVar1);
    if (iVar3 < 0) goto LAB_ffff0000907cbff4;
  }
  goto LAB_ffff0000907cc094;
  while( true ) {
    iVar3 = FUN_ffff0000907c8058(&local_58,&local_58,lVar1);
    if (iVar3 != 0) break;
LAB_ffff0000907cbf0c:
    iVar3 = FUN_ffff0000907c7c44(&local_58,lVar1);
    if (iVar3 < 0) goto LAB_ffff0000907cbff4;
  }
  goto LAB_ffff0000907cc094;
LAB_ffff0000907cbff4:
  lVar1 = param_3 + 0x18;
  iVar3 = FUN_ffff0000907c846c(&local_88,lVar1,lVar1);
  if (iVar3 == 0) {
    iVar3 = FUN_ffff0000907cc6c4(&local_88,param_1);
    if (iVar3 == 0) {
      iVar3 = FUN_ffff0000907c787c(&local_88,1);
      if (iVar3 == 0) {
        lVar2 = param_1 + 8;
        do {
          iVar3 = FUN_ffff0000907c7c44(&local_88,lVar2);
          if (iVar3 < 0) {
            iVar3 = FUN_ffff0000907c846c(&local_70,param_3,&local_88);
            if (iVar3 == 0) {
              iVar3 = FUN_ffff0000907cc6c4(&local_70,param_1);
              if (iVar3 == 0) {
                iVar3 = FUN_ffff0000907c787c(&local_70,1);
                if (iVar3 == 0) goto LAB_ffff0000907cc0e8;
              }
            }
            break;
          }
          iVar3 = FUN_ffff0000907c8058(&local_88,&local_88,lVar2);
        } while (iVar3 == 0);
      }
    }
  }
  goto LAB_ffff0000907cc094;
  while( true ) {
    iVar3 = FUN_ffff0000907c8058(&local_70,&local_70,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000907cc0e8:
    iVar3 = FUN_ffff0000907c7c44(&local_70,lVar2);
    if (iVar3 < 0) {
      iVar3 = FUN_ffff0000907c846c(&local_a0,&local_88,&local_88);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000907cc6c4(&local_a0,param_1);
        if (iVar3 == 0) {
          iVar3 = FUN_ffff0000907c787c(&local_a0,1);
          if (iVar3 == 0) goto LAB_ffff0000907cc154;
        }
      }
      break;
    }
  }
  goto LAB_ffff0000907cc094;
  while( true ) {
    iVar3 = FUN_ffff0000907c8058(&local_a0,&local_a0,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000907cc154:
    iVar3 = FUN_ffff0000907c7c44(&local_a0,lVar2);
    if (iVar3 < 0) {
      iVar3 = FUN_ffff0000907c846c(&local_88,&local_58,&local_58);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000907cc6c4(&local_88,param_1);
        if (iVar3 == 0) {
          iVar3 = FUN_ffff0000907c8358(&local_88,&local_88,&local_70);
          if (iVar3 == 0) goto LAB_ffff0000907cc1c4;
        }
      }
      break;
    }
  }
  goto LAB_ffff0000907cc094;
  while( true ) {
    iVar3 = FUN_ffff0000907c8248(&local_88,&local_88,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000907cc1c4:
    if ((-1 < (short)local_88) || (iVar3 = FUN_ffff0000907c7d34(&local_88,0), iVar3 == 0)) {
      iVar3 = FUN_ffff0000907c8358(&local_88,&local_88,&local_70);
      if (iVar3 == 0) goto LAB_ffff0000907cc210;
      break;
    }
  }
  goto LAB_ffff0000907cc094;
  while( true ) {
    iVar3 = FUN_ffff0000907c8248(&local_88,&local_88,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000907cc210:
    if ((-1 < (short)local_88) || (iVar3 = FUN_ffff0000907c7d34(&local_88,0), iVar3 == 0)) {
      iVar3 = FUN_ffff0000907c8358(&local_70,&local_70,&local_88);
      if (iVar3 == 0) goto LAB_ffff0000907cc25c;
      break;
    }
  }
  goto LAB_ffff0000907cc094;
  while( true ) {
    iVar3 = FUN_ffff0000907c8248(&local_70,&local_70,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000907cc25c:
    if ((-1 < (short)local_70) || (iVar3 = FUN_ffff0000907c7d34(&local_70,0), iVar3 == 0)) {
      iVar3 = FUN_ffff0000907c846c(&local_70,&local_70,&local_58);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000907cc6c4(&local_70,param_1);
        if (iVar3 == 0) {
          iVar3 = FUN_ffff0000907c8358(&local_70,&local_70,&local_a0);
          if (iVar3 == 0) goto LAB_ffff0000907cc2d4;
        }
      }
      break;
    }
  }
  goto LAB_ffff0000907cc094;
  while( true ) {
    iVar3 = FUN_ffff0000907c8248(&local_70,&local_70,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000907cc2d4:
    if ((-1 < (short)local_70) || (iVar3 = FUN_ffff0000907c7d34(&local_70,0), iVar3 == 0)) {
      iVar3 = FUN_ffff0000907c846c(&local_a0,lVar1,param_3 + 0x30);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000907cc6c4(&local_a0,param_1);
        if (iVar3 == 0) {
          iVar3 = FUN_ffff0000907c787c(&local_a0,1);
          if (iVar3 == 0) goto LAB_ffff0000907cc348;
        }
      }
      break;
    }
  }
  goto LAB_ffff0000907cc094;
  while( true ) {
    iVar3 = FUN_ffff0000907c8058(&local_a0,&local_a0,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000907cc348:
    iVar3 = FUN_ffff0000907c7c44(&local_a0,lVar2);
    if (iVar3 < 0) {
      iVar3 = FUN_ffff0000907c7260(param_2,&local_88);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000907c7260(param_2 + 0x18,&local_70);
        if (iVar3 == 0) {
          iVar3 = FUN_ffff0000907c7260(param_2 + 0x30,&local_a0);
        }
      }
      break;
    }
  }
LAB_ffff0000907cc094:
  FUN_ffff0000907c7058(&local_58);
  FUN_ffff0000907c7058(&local_70);
  FUN_ffff0000907c7058(&local_88);
  FUN_ffff0000907c7058(&local_a0);
  return iVar3;
}


