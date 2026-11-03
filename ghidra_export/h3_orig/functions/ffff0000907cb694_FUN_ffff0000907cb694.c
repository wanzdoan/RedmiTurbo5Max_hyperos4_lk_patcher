// ffff0000907cb694  size=1704  FUN_ffff0000907cb694

int FUN_ffff0000907cb694(long param_1,long param_2,long param_3)

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
  FUN_ffff0000907c69cc(&local_58);
  FUN_ffff0000907c69cc(&local_70);
  FUN_ffff0000907c69cc(&local_88);
  FUN_ffff0000907c69cc(&local_a0);
  if (*(long *)(param_1 + 0x30) == 0) {
    iVar3 = FUN_ffff0000907c7df8(&local_70,param_3 + 0x30,param_3 + 0x30);
    if (iVar3 == 0) {
      iVar3 = FUN_ffff0000907cc050(&local_70,param_1);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000907c7bd4(&local_88,param_3,&local_70);
        if (iVar3 == 0) {
          lVar1 = param_1 + 8;
          do {
            iVar3 = FUN_ffff0000907c75d0(&local_88,lVar1);
            if (iVar3 < 0) {
              iVar3 = FUN_ffff0000907c7ce4(&local_a0,param_3,&local_70);
              if (iVar3 == 0) goto LAB_ffff0000907cb8dc;
              break;
            }
            iVar3 = FUN_ffff0000907c79e4(&local_88,&local_88,lVar1);
          } while (iVar3 == 0);
        }
      }
    }
  }
  else {
    iVar3 = FUN_ffff0000907c7df8(&local_70,param_3,param_3);
    if (iVar3 == 0) {
      iVar3 = FUN_ffff0000907cc050(&local_70,param_1);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000907c7040(&local_58,&local_70,3);
        if (iVar3 == 0) {
          lVar1 = param_1 + 8;
          do {
            iVar3 = FUN_ffff0000907c75d0(&local_58,lVar1);
            if (iVar3 < 0) {
              iVar3 = FUN_ffff0000907c76c0(param_1 + 0x20,0);
              if (iVar3 == 0) goto LAB_ffff0000907cb980;
              iVar3 = FUN_ffff0000907c7df8(&local_70,param_3 + 0x30,param_3 + 0x30);
              if (iVar3 == 0) {
                iVar3 = FUN_ffff0000907cc050(&local_70,param_1);
                if (iVar3 == 0) {
                  iVar3 = FUN_ffff0000907c7df8(&local_88,&local_70,&local_70);
                  if (iVar3 == 0) {
                    iVar3 = FUN_ffff0000907cc050(&local_88,param_1);
                    if (iVar3 == 0) {
                      iVar3 = FUN_ffff0000907c7df8(&local_70,&local_88,param_1 + 0x20);
                      if (iVar3 == 0) {
                        iVar3 = FUN_ffff0000907cc050(&local_70,param_1);
                        if (iVar3 == 0) {
                          iVar3 = FUN_ffff0000907c7bd4(&local_58,&local_58,&local_70);
                          if (iVar3 == 0) goto LAB_ffff0000907cb898;
                        }
                      }
                    }
                  }
                }
              }
              break;
            }
            iVar3 = FUN_ffff0000907c79e4(&local_58,&local_58,lVar1);
          } while (iVar3 == 0);
        }
      }
    }
  }
  goto LAB_ffff0000907cba20;
  while( true ) {
    iVar3 = FUN_ffff0000907c7bd4(&local_a0,&local_a0,lVar1);
    if (iVar3 != 0) break;
LAB_ffff0000907cb8dc:
    if ((-1 < (short)local_a0) || (iVar3 = FUN_ffff0000907c76c0(&local_a0,0), iVar3 == 0)) {
      iVar3 = FUN_ffff0000907c7df8(&local_70,&local_88,&local_a0);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000907cc050(&local_70,param_1);
        if (iVar3 == 0) {
          iVar3 = FUN_ffff0000907c7040(&local_58,&local_70,3);
          if (iVar3 == 0) goto LAB_ffff0000907cb954;
        }
      }
      break;
    }
  }
  goto LAB_ffff0000907cba20;
  while( true ) {
    iVar3 = FUN_ffff0000907c79e4(&local_58,&local_58,lVar1);
    if (iVar3 != 0) break;
LAB_ffff0000907cb954:
    iVar3 = FUN_ffff0000907c75d0(&local_58,lVar1);
    if (iVar3 < 0) goto LAB_ffff0000907cb980;
  }
  goto LAB_ffff0000907cba20;
  while( true ) {
    iVar3 = FUN_ffff0000907c79e4(&local_58,&local_58,lVar1);
    if (iVar3 != 0) break;
LAB_ffff0000907cb898:
    iVar3 = FUN_ffff0000907c75d0(&local_58,lVar1);
    if (iVar3 < 0) goto LAB_ffff0000907cb980;
  }
  goto LAB_ffff0000907cba20;
LAB_ffff0000907cb980:
  lVar1 = param_3 + 0x18;
  iVar3 = FUN_ffff0000907c7df8(&local_88,lVar1,lVar1);
  if (iVar3 == 0) {
    iVar3 = FUN_ffff0000907cc050(&local_88,param_1);
    if (iVar3 == 0) {
      iVar3 = FUN_ffff0000907c7208(&local_88,1);
      if (iVar3 == 0) {
        lVar2 = param_1 + 8;
        do {
          iVar3 = FUN_ffff0000907c75d0(&local_88,lVar2);
          if (iVar3 < 0) {
            iVar3 = FUN_ffff0000907c7df8(&local_70,param_3,&local_88);
            if (iVar3 == 0) {
              iVar3 = FUN_ffff0000907cc050(&local_70,param_1);
              if (iVar3 == 0) {
                iVar3 = FUN_ffff0000907c7208(&local_70,1);
                if (iVar3 == 0) goto LAB_ffff0000907cba74;
              }
            }
            break;
          }
          iVar3 = FUN_ffff0000907c79e4(&local_88,&local_88,lVar2);
        } while (iVar3 == 0);
      }
    }
  }
  goto LAB_ffff0000907cba20;
  while( true ) {
    iVar3 = FUN_ffff0000907c79e4(&local_70,&local_70,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000907cba74:
    iVar3 = FUN_ffff0000907c75d0(&local_70,lVar2);
    if (iVar3 < 0) {
      iVar3 = FUN_ffff0000907c7df8(&local_a0,&local_88,&local_88);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000907cc050(&local_a0,param_1);
        if (iVar3 == 0) {
          iVar3 = FUN_ffff0000907c7208(&local_a0,1);
          if (iVar3 == 0) goto LAB_ffff0000907cbae0;
        }
      }
      break;
    }
  }
  goto LAB_ffff0000907cba20;
  while( true ) {
    iVar3 = FUN_ffff0000907c79e4(&local_a0,&local_a0,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000907cbae0:
    iVar3 = FUN_ffff0000907c75d0(&local_a0,lVar2);
    if (iVar3 < 0) {
      iVar3 = FUN_ffff0000907c7df8(&local_88,&local_58,&local_58);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000907cc050(&local_88,param_1);
        if (iVar3 == 0) {
          iVar3 = FUN_ffff0000907c7ce4(&local_88,&local_88,&local_70);
          if (iVar3 == 0) goto LAB_ffff0000907cbb50;
        }
      }
      break;
    }
  }
  goto LAB_ffff0000907cba20;
  while( true ) {
    iVar3 = FUN_ffff0000907c7bd4(&local_88,&local_88,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000907cbb50:
    if ((-1 < (short)local_88) || (iVar3 = FUN_ffff0000907c76c0(&local_88,0), iVar3 == 0)) {
      iVar3 = FUN_ffff0000907c7ce4(&local_88,&local_88,&local_70);
      if (iVar3 == 0) goto LAB_ffff0000907cbb9c;
      break;
    }
  }
  goto LAB_ffff0000907cba20;
  while( true ) {
    iVar3 = FUN_ffff0000907c7bd4(&local_88,&local_88,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000907cbb9c:
    if ((-1 < (short)local_88) || (iVar3 = FUN_ffff0000907c76c0(&local_88,0), iVar3 == 0)) {
      iVar3 = FUN_ffff0000907c7ce4(&local_70,&local_70,&local_88);
      if (iVar3 == 0) goto LAB_ffff0000907cbbe8;
      break;
    }
  }
  goto LAB_ffff0000907cba20;
  while( true ) {
    iVar3 = FUN_ffff0000907c7bd4(&local_70,&local_70,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000907cbbe8:
    if ((-1 < (short)local_70) || (iVar3 = FUN_ffff0000907c76c0(&local_70,0), iVar3 == 0)) {
      iVar3 = FUN_ffff0000907c7df8(&local_70,&local_70,&local_58);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000907cc050(&local_70,param_1);
        if (iVar3 == 0) {
          iVar3 = FUN_ffff0000907c7ce4(&local_70,&local_70,&local_a0);
          if (iVar3 == 0) goto LAB_ffff0000907cbc60;
        }
      }
      break;
    }
  }
  goto LAB_ffff0000907cba20;
  while( true ) {
    iVar3 = FUN_ffff0000907c7bd4(&local_70,&local_70,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000907cbc60:
    if ((-1 < (short)local_70) || (iVar3 = FUN_ffff0000907c76c0(&local_70,0), iVar3 == 0)) {
      iVar3 = FUN_ffff0000907c7df8(&local_a0,lVar1,param_3 + 0x30);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000907cc050(&local_a0,param_1);
        if (iVar3 == 0) {
          iVar3 = FUN_ffff0000907c7208(&local_a0,1);
          if (iVar3 == 0) goto LAB_ffff0000907cbcd4;
        }
      }
      break;
    }
  }
  goto LAB_ffff0000907cba20;
  while( true ) {
    iVar3 = FUN_ffff0000907c79e4(&local_a0,&local_a0,lVar2);
    if (iVar3 != 0) break;
LAB_ffff0000907cbcd4:
    iVar3 = FUN_ffff0000907c75d0(&local_a0,lVar2);
    if (iVar3 < 0) {
      iVar3 = FUN_ffff0000907c6bec(param_2,&local_88);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000907c6bec(param_2 + 0x18,&local_70);
        if (iVar3 == 0) {
          iVar3 = FUN_ffff0000907c6bec(param_2 + 0x30,&local_a0);
        }
      }
      break;
    }
  }
LAB_ffff0000907cba20:
  FUN_ffff0000907c69e4(&local_58);
  FUN_ffff0000907c69e4(&local_70);
  FUN_ffff0000907c69e4(&local_88);
  FUN_ffff0000907c69e4(&local_a0);
  return iVar3;
}


