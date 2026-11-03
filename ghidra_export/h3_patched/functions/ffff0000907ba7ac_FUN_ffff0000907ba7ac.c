// ffff0000907ba7ac  size=504  FUN_ffff0000907ba7ac

int FUN_ffff0000907ba7ac(undefined8 param_1,int param_2,long param_3,long param_4,long param_5)

{
  long lVar1;
  int iVar2;
  undefined *puVar3;
  long lVar4;
  undefined8 local_208;
  undefined8 uStack_200;
  undefined8 local_1f8;
  undefined8 uStack_1f0;
  undefined1 *local_1e8;
  undefined8 uStack_1e0;
  undefined8 local_1d8;
  undefined8 uStack_1d0;
  undefined8 local_1c8;
  undefined8 uStack_1c0;
  undefined1 *local_1b8;
  undefined8 uStack_1b0;
  int local_1a8;
  undefined1 auStack_1a4 [48];
  undefined1 auStack_174 [48];
  undefined1 auStack_144 [240];
  int local_54;
  
  iVar2 = 0x7247;
  local_54 = 0xff;
  if (((param_3 != 0) && (param_4 != 0)) && (param_5 != 0)) {
    lVar4 = 0x30;
    if (param_2 != 4) {
      lVar4 = 0;
    }
    lVar1 = 0x20;
    if (param_2 != 0) {
      lVar1 = lVar4;
    }
    if (lVar1 == 0) {
      iVar2 = 0x7243;
    }
    else {
      lVar4 = 0;
      local_1a8 = 0;
      do {
        if (*(char *)(param_3 + lVar4) != '\0') {
          if (param_2 == 4) {
            puVar3 = &DAT_ffff0000908134e8;
          }
          else {
            if (param_2 != 0) {
              local_54 = 0x7240;
              goto LAB_ffff0000907ba88c;
            }
            puVar3 = &DAT_ffff0000908134c8;
          }
          local_1a8 = FUN_ffff0000907bb4fc(param_3,puVar3,lVar1);
          local_54 = 0;
          if (-1 < local_1a8) {
            local_54 = 0x7263;
          }
          goto LAB_ffff0000907ba88c;
        }
        lVar4 = lVar4 + 1;
      } while (lVar1 != lVar4);
      local_54 = 0x7263;
LAB_ffff0000907ba88c:
      iVar2 = local_54;
      if (local_54 == 0) {
        FUN_ffff00009077eb88(&local_1a8,0,0x154);
        local_1d8 = 0;
        uStack_1d0 = 0;
        local_1c8 = 0;
        uStack_1c0 = 0;
        local_1b8 = (undefined1 *)0x0;
        uStack_1b0 = 0;
        FUN_ffff00009077eb88(&local_1d8,0,0x30);
        FUN_ffff00009077eb88(&local_1a8,0,0x154);
        FUN_ffff00009077e858(auStack_1a4,param_3,lVar1);
        local_1d8 = 0;
        uStack_1d0 = 0;
        local_1c8 = 0;
        uStack_1c0 = 0;
        uStack_1b0 = 0;
        local_54 = 0;
        local_1b8 = auStack_1a4;
        if (param_2 == 2) {
          local_54 = 0x7240;
        }
        else {
          local_1f8 = 0;
          uStack_1f0 = 0;
          uStack_1e0 = 0;
          local_208 = 0;
          uStack_200 = 0;
          local_1e8 = auStack_1a4;
          local_54 = FUN_ffff0000907ba9a4(param_1,param_2,5,&local_208,auStack_174,auStack_144);
        }
        iVar2 = local_54;
        if (local_54 == 0) {
          FUN_ffff00009077e858(param_4,auStack_174,lVar1);
          FUN_ffff00009077e858(param_5,auStack_144,lVar1);
          iVar2 = local_54;
        }
      }
    }
  }
  return iVar2;
}


