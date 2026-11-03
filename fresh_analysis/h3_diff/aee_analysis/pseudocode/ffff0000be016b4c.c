
undefined8 FUN_ffff0000be016b4c(undefined8 param_1,undefined8 *param_2)

{
  uint uVar1;
  long lVar2;
  ulong uVar3;
  long lVar4;
  undefined8 uVar5;
  long *plVar6;
  undefined8 uVar7;
  ulong uVar8;
  undefined8 *puVar9;
  undefined4 local_28;
  undefined1 local_24;
  
  local_24 = 0;
  local_28 = 0xa0d0920;
  DAT_ffff0000be107b68 = FUN_ffff0000be016c48(param_1,&local_28);
  if (DAT_ffff0000be107b68 != 0) {
    DAT_ffff0000be107b70 = FUN_ffff0000be016c48(0,&local_28);
    uVar1 = FUN_ffff0000be05f5e0(DAT_ffff0000be107b70,0,10);
    if (uVar1 < 0x59) {
      uVar8 = (ulong)(uVar1 + 2);
      if (uVar1 != 0) {
        lVar4 = uVar8 - 2;
        plVar6 = &DAT_ffff0000be107b78;
        do {
          lVar2 = FUN_ffff0000be016c48(0,&local_28);
          *plVar6 = lVar2;
          if (lVar2 == 0) {
            return 1;
          }
          uVar3 = FUN_ffff0000be060e5c();
          if (0x13 < uVar3) {
            return 1;
          }
          plVar6 = plVar6 + 1;
          lVar4 = lVar4 + -1;
        } while (lVar4 != 0);
      }
      puVar9 = &DAT_ffff0000be107b68;
      do {
        uVar7 = *puVar9;
        uVar5 = *param_2;
        lVar4 = FUN_ffff0000be060e5c(uVar7);
        FUN_ffff0000be060eb8(uVar5,uVar7,lVar4 + 1);
        uVar8 = uVar8 - 1;
        param_2 = param_2 + 1;
        puVar9 = puVar9 + 1;
      } while (uVar8 != 0);
      return 0;
    }
  }
  return 1;
}


