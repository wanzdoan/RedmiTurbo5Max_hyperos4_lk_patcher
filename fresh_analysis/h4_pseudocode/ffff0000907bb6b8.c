
int FUN_ffff0000907bb6b8
              (undefined8 param_1,int param_2,long param_3,long param_4,long param_5,long param_6,
              long param_7)

{
  long lVar1;
  int iVar2;
  undefined *puVar3;
  undefined8 uVar4;
  long lVar5;
  undefined8 local_220;
  undefined8 uStack_218;
  undefined8 local_210;
  undefined1 *puStack_208;
  undefined1 *local_200;
  undefined1 *puStack_1f8;
  undefined8 local_1f0;
  undefined8 uStack_1e8;
  undefined8 local_1e0;
  undefined1 *puStack_1d8;
  undefined1 *local_1d0;
  undefined1 *puStack_1c8;
  int local_1c0;
  undefined1 auStack_1bc [144];
  undefined1 auStack_12c [48];
  undefined1 auStack_fc [48];
  undefined1 auStack_cc [48];
  undefined1 auStack_9c [48];
  int local_6c;
  
  iVar2 = 0x7247;
  local_6c = 0xff;
  if ((((param_3 != 0) && (param_4 != 0)) && (param_5 != 0)) && ((param_6 != 0 && (param_7 != 0))))
  {
    lVar5 = 0x30;
    if (param_2 != 4) {
      lVar5 = 0;
    }
    lVar1 = 0x20;
    if (param_2 != 0) {
      lVar1 = lVar5;
    }
    if (lVar1 == 0) {
      iVar2 = 0x7243;
    }
    else {
      lVar5 = 0;
      local_1c0 = 0;
      do {
        if (*(char *)(param_3 + lVar5) != '\0') {
          if (param_2 == 4) {
            puVar3 = &DAT_ffff000090813710;
          }
          else {
            if (param_2 != 0) {
              local_6c = 0x7240;
              goto LAB_ffff0000907bb7ac;
            }
            puVar3 = &DAT_ffff0000908136f0;
          }
          iVar2 = FUN_ffff0000907bbb70(param_3,puVar3,lVar1);
          local_6c = 0;
          if (-1 < iVar2) {
            local_6c = 0x7263;
          }
          goto LAB_ffff0000907bb7ac;
        }
        lVar5 = lVar5 + 1;
      } while (lVar1 != lVar5);
      local_6c = 0x7263;
LAB_ffff0000907bb7ac:
      iVar2 = local_6c;
      if (local_6c == 0) {
        lVar5 = 0;
        local_1c0 = 0;
        do {
          if (*(char *)(param_4 + lVar5) != '\0') {
            if (param_2 == 4) {
              puVar3 = &DAT_ffff000090813710;
            }
            else {
              if (param_2 != 0) {
                local_6c = 0x7240;
                goto LAB_ffff0000907bb84c;
              }
              puVar3 = &DAT_ffff0000908136f0;
            }
            local_1c0 = FUN_ffff0000907bbb70(param_4,puVar3,lVar1);
            local_6c = 0;
            if (-1 < local_1c0) {
              local_6c = 0x7263;
            }
            goto LAB_ffff0000907bb84c;
          }
          lVar5 = lVar5 + 1;
        } while (lVar1 != lVar5);
        local_6c = 0x7263;
LAB_ffff0000907bb84c:
        if (local_6c == 0x7263) {
          iVar2 = 0x7265;
        }
        else {
          iVar2 = local_6c;
          if (local_6c == 0) {
            FUN_ffff00009077ee50(&local_1c0,0,0x154);
            local_1f0 = 0;
            uStack_1e8 = 0;
            local_1e0 = 0;
            puStack_1d8 = (undefined1 *)0x0;
            local_1d0 = (undefined1 *)0x0;
            puStack_1c8 = (undefined1 *)0x0;
            FUN_ffff00009077ee50(&local_1f0,0,0x30);
            FUN_ffff00009077ee50(&local_1c0,0,0x154);
            FUN_ffff00009077eb20(auStack_1bc,param_3,lVar1);
            FUN_ffff00009077eb20(auStack_12c,param_4,lVar1);
            FUN_ffff00009077eb20(auStack_fc,param_5,lVar1);
            local_1f0 = 0;
            uStack_1e8 = 0;
            local_1e0 = 0;
            local_6c = 0;
            puStack_1d8 = auStack_1bc;
            local_1d0 = auStack_12c;
            puStack_1c8 = auStack_fc;
            if (param_2 == 2) {
              local_6c = 0x7240;
            }
            else {
              if (param_2 == 1) {
                param_2 = 1;
                uVar4 = 7;
              }
              else {
                uVar4 = 6;
              }
              local_210 = 0;
              local_220 = 0;
              uStack_218 = 0;
              puStack_208 = auStack_1bc;
              local_200 = auStack_12c;
              puStack_1f8 = auStack_fc;
              local_6c = FUN_ffff0000907bb018
                                   (param_1,param_2,uVar4,&local_220,auStack_cc,auStack_9c);
            }
            iVar2 = local_6c;
            if (local_6c == 0) {
              FUN_ffff00009077eb20(param_6,auStack_cc,lVar1);
              FUN_ffff00009077eb20(param_7,auStack_9c,lVar1);
              iVar2 = local_6c;
            }
          }
        }
      }
    }
  }
  return iVar2;
}


