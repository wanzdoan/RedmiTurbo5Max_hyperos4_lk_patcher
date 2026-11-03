
ulong FUN_ffff0000907cb568(long param_1,long param_2,long param_3,long param_4)

{
  long lVar1;
  int iVar2;
  uint uVar3;
  long lVar4;
  ulong uVar5;
  undefined8 local_f8;
  undefined8 uStack_f0;
  undefined8 local_e8;
  undefined8 local_e0;
  undefined8 local_d8;
  undefined8 uStack_d0;
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 local_68;
  undefined8 local_60;
  undefined8 uStack_58;
  
  lVar4 = param_3 + 0x30;
  iVar2 = FUN_ffff0000907c7d34(lVar4,0);
  if (iVar2 == 0) {
    uVar3 = FUN_ffff0000907c7260(param_2,param_4);
    if (uVar3 != 0) {
      return (ulong)uVar3;
    }
    uVar3 = FUN_ffff0000907c7260(param_2 + 0x18,param_4 + 0x18);
    if (uVar3 != 0) {
      return (ulong)uVar3;
    }
    lVar4 = param_4 + 0x30;
LAB_ffff0000907cb7dc:
    uVar5 = FUN_ffff0000907c7260(param_2 + 0x30,lVar4);
    return uVar5;
  }
  if (*(long *)(param_4 + 0x40) != 0) {
    iVar2 = FUN_ffff0000907c7d34(param_4 + 0x30,0);
    if (iVar2 == 0) {
      uVar3 = FUN_ffff0000907c7260(param_2,param_3);
      if (uVar3 != 0) {
        return (ulong)uVar3;
      }
      uVar3 = FUN_ffff0000907c7260(param_2 + 0x18,param_3 + 0x18);
      if (uVar3 != 0) {
        return (ulong)uVar3;
      }
      goto LAB_ffff0000907cb7dc;
    }
    if ((*(long *)(param_4 + 0x40) != 0) &&
       (iVar2 = FUN_ffff0000907c7d34(param_4 + 0x30,1), iVar2 != 0)) {
      return 0xffffb080;
    }
  }
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  local_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_98 = 0;
  uStack_90 = 0;
  local_88 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_a0 = 0;
  local_c8 = 0;
  uStack_c0 = 0;
  local_b8 = 0;
  local_d8 = 0;
  uStack_d0 = 0;
  local_e8 = 0;
  local_e0 = 0;
  local_f8 = 0;
  uStack_f0 = 0;
  FUN_ffff0000907c7040(&local_68);
  FUN_ffff0000907c7040(&local_80);
  FUN_ffff0000907c7040(&local_98);
  FUN_ffff0000907c7040(&local_b0);
  FUN_ffff0000907c7040(&local_c8);
  FUN_ffff0000907c7040(&local_e0);
  FUN_ffff0000907c7040(&local_f8);
  uVar3 = FUN_ffff0000907c846c(&local_68,lVar4,lVar4);
  uVar5 = (ulong)uVar3;
  if (uVar3 == 0) {
    uVar3 = FUN_ffff0000907cc6c4(&local_68,param_1);
    uVar5 = (ulong)uVar3;
    if (uVar3 == 0) {
      uVar3 = FUN_ffff0000907c846c(&local_80,&local_68,lVar4);
      uVar5 = (ulong)uVar3;
      if (uVar3 == 0) {
        uVar3 = FUN_ffff0000907cc6c4(&local_80,param_1);
        uVar5 = (ulong)uVar3;
        if (uVar3 == 0) {
          uVar3 = FUN_ffff0000907c846c(&local_68,&local_68,param_4);
          uVar5 = (ulong)uVar3;
          if (uVar3 == 0) {
            uVar3 = FUN_ffff0000907cc6c4(&local_68,param_1);
            uVar5 = (ulong)uVar3;
            if (uVar3 == 0) {
              uVar3 = FUN_ffff0000907c846c(&local_80,&local_80,param_4 + 0x18);
              uVar5 = (ulong)uVar3;
              if (uVar3 == 0) {
                uVar3 = FUN_ffff0000907cc6c4(&local_80,param_1);
                uVar5 = (ulong)uVar3;
                if (uVar3 == 0) {
                  uVar3 = FUN_ffff0000907c8358(&local_68,&local_68,param_3);
                  uVar5 = (ulong)uVar3;
                  if (uVar3 == 0) {
                    lVar1 = param_1 + 8;
                    do {
                      if ((-1 < (short)local_68) ||
                         (iVar2 = FUN_ffff0000907c7d34(&local_68,0), iVar2 == 0)) {
                        uVar3 = FUN_ffff0000907c8358(&local_80,&local_80,param_3 + 0x18);
                        uVar5 = (ulong)uVar3;
                        if (uVar3 == 0) goto LAB_ffff0000907cb850;
                        break;
                      }
                      uVar3 = FUN_ffff0000907c8248(&local_68,&local_68,lVar1);
                      uVar5 = (ulong)uVar3;
                    } while (uVar3 == 0);
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  goto LAB_ffff0000907cb718;
  while( true ) {
    uVar3 = FUN_ffff0000907c8248(&local_80,&local_80,lVar1);
    uVar5 = (ulong)uVar3;
    if (uVar3 != 0) break;
LAB_ffff0000907cb850:
    if ((-1 < (short)local_80) || (iVar2 = FUN_ffff0000907c7d34(&local_80,0), iVar2 == 0)) {
      iVar2 = FUN_ffff0000907c7d34(&local_68,0);
      if (iVar2 == 0) {
        iVar2 = FUN_ffff0000907c7d34(&local_80,0);
        if (iVar2 == 0) {
          uVar5 = FUN_ffff0000907cbd08(param_1,param_2,param_3);
        }
        else {
          uVar5 = FUN_ffff0000907ca758(param_2);
        }
        goto LAB_ffff0000907cb9b4;
      }
      uVar3 = FUN_ffff0000907c846c(&local_f8,lVar4,&local_68);
      uVar5 = (ulong)uVar3;
      if (uVar3 == 0) {
        uVar3 = FUN_ffff0000907cc6c4(&local_f8,param_1);
        uVar5 = (ulong)uVar3;
        if (uVar3 == 0) {
          uVar3 = FUN_ffff0000907c846c(&local_98,&local_68,&local_68);
          uVar5 = (ulong)uVar3;
          if (uVar3 == 0) {
            uVar3 = FUN_ffff0000907cc6c4(&local_98,param_1);
            uVar5 = (ulong)uVar3;
            if (uVar3 == 0) {
              uVar3 = FUN_ffff0000907c846c(&local_b0,&local_98,&local_68);
              uVar5 = (ulong)uVar3;
              if (uVar3 == 0) {
                uVar3 = FUN_ffff0000907cc6c4(&local_b0,param_1);
                uVar5 = (ulong)uVar3;
                if (uVar3 == 0) {
                  uVar3 = FUN_ffff0000907c846c(&local_98,&local_98,param_3);
                  uVar5 = (ulong)uVar3;
                  if (uVar3 == 0) {
                    uVar3 = FUN_ffff0000907cc6c4(&local_98,param_1);
                    uVar5 = (ulong)uVar3;
                    if (uVar3 == 0) {
                      uVar3 = FUN_ffff0000907c76b4(&local_68,&local_98,2);
                      uVar5 = (ulong)uVar3;
                      if (uVar3 == 0) goto LAB_ffff0000907cb95c;
                    }
                  }
                }
              }
            }
          }
        }
      }
      break;
    }
  }
  goto LAB_ffff0000907cb718;
  while( true ) {
    uVar3 = FUN_ffff0000907c8058(&local_68,&local_68,lVar1);
    uVar5 = (ulong)uVar3;
    if (uVar3 != 0) break;
LAB_ffff0000907cb95c:
    iVar2 = FUN_ffff0000907c7c44(&local_68,lVar1);
    if (iVar2 < 0) {
      uVar3 = FUN_ffff0000907c846c(&local_c8,&local_80,&local_80);
      uVar5 = (ulong)uVar3;
      if (uVar3 == 0) {
        uVar3 = FUN_ffff0000907cc6c4(&local_c8,param_1);
        uVar5 = (ulong)uVar3;
        if (uVar3 == 0) {
          uVar3 = FUN_ffff0000907c8358(&local_c8,&local_c8,&local_68);
          uVar5 = (ulong)uVar3;
          if (uVar3 == 0) goto LAB_ffff0000907cba00;
        }
      }
      break;
    }
  }
  goto LAB_ffff0000907cb718;
  while( true ) {
    uVar3 = FUN_ffff0000907c8248(&local_c8,&local_c8,lVar1);
    uVar5 = (ulong)uVar3;
    if (uVar3 != 0) break;
LAB_ffff0000907cba00:
    if ((-1 < (short)local_c8) || (iVar2 = FUN_ffff0000907c7d34(&local_c8,0), iVar2 == 0)) {
      uVar3 = FUN_ffff0000907c8358(&local_c8,&local_c8,&local_b0);
      uVar5 = (ulong)uVar3;
      if (uVar3 == 0) goto LAB_ffff0000907cba4c;
      break;
    }
  }
  goto LAB_ffff0000907cb718;
  while( true ) {
    uVar3 = FUN_ffff0000907c8248(&local_c8,&local_c8,lVar1);
    uVar5 = (ulong)uVar3;
    if (uVar3 != 0) break;
LAB_ffff0000907cba4c:
    if ((-1 < (short)local_c8) || (iVar2 = FUN_ffff0000907c7d34(&local_c8,0), iVar2 == 0)) {
      uVar3 = FUN_ffff0000907c8358(&local_98,&local_98,&local_c8);
      uVar5 = (ulong)uVar3;
      if (uVar3 == 0) goto LAB_ffff0000907cba98;
      break;
    }
  }
  goto LAB_ffff0000907cb718;
  while( true ) {
    uVar3 = FUN_ffff0000907c8248(&local_98,&local_98,lVar1);
    uVar5 = (ulong)uVar3;
    if (uVar3 != 0) break;
LAB_ffff0000907cba98:
    if ((-1 < (short)local_98) || (iVar2 = FUN_ffff0000907c7d34(&local_98,0), iVar2 == 0)) {
      uVar3 = FUN_ffff0000907c846c(&local_98,&local_98,&local_80);
      uVar5 = (ulong)uVar3;
      if (uVar3 == 0) {
        uVar3 = FUN_ffff0000907cc6c4(&local_98,param_1);
        uVar5 = (ulong)uVar3;
        if (uVar3 == 0) {
          uVar3 = FUN_ffff0000907c846c(&local_b0,&local_b0,param_3 + 0x18);
          uVar5 = (ulong)uVar3;
          if (uVar3 == 0) {
            uVar3 = FUN_ffff0000907cc6c4(&local_b0,param_1);
            uVar5 = (ulong)uVar3;
            if (uVar3 == 0) {
              uVar3 = FUN_ffff0000907c8358(&local_e0,&local_98,&local_b0);
              uVar5 = (ulong)uVar3;
              if (uVar3 == 0) goto LAB_ffff0000907cbb3c;
            }
          }
        }
      }
      break;
    }
  }
  goto LAB_ffff0000907cb718;
  while( true ) {
    uVar3 = FUN_ffff0000907c8248(&local_e0,&local_e0,lVar1);
    uVar5 = (ulong)uVar3;
    if (uVar3 != 0) break;
LAB_ffff0000907cbb3c:
    if ((-1 < (short)local_e0) || (iVar2 = FUN_ffff0000907c7d34(&local_e0,0), iVar2 == 0)) {
      uVar3 = FUN_ffff0000907c7260(param_2,&local_c8);
      uVar5 = (ulong)uVar3;
      if (uVar3 == 0) {
        uVar3 = FUN_ffff0000907c7260(param_2 + 0x18,&local_e0);
        uVar5 = (ulong)uVar3;
        if (uVar3 == 0) {
          uVar5 = FUN_ffff0000907c7260(param_2 + 0x30,&local_f8);
LAB_ffff0000907cb9b4:
          uVar5 = uVar5 & 0xffffffff;
        }
      }
      break;
    }
  }
LAB_ffff0000907cb718:
  FUN_ffff0000907c7058(&local_68);
  FUN_ffff0000907c7058(&local_80);
  FUN_ffff0000907c7058(&local_98);
  FUN_ffff0000907c7058(&local_b0);
  FUN_ffff0000907c7058(&local_c8);
  FUN_ffff0000907c7058(&local_e0);
  FUN_ffff0000907c7058(&local_f8);
  return uVar5;
}


