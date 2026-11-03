
undefined8 FUN_ffff0000be041988(long param_1)

{
  uint uVar1;
  undefined4 *puVar2;
  code *pcVar3;
  ulong uVar4;
  uint uVar5;
  long *plVar6;
  ulong uVar7;
  undefined4 *puVar8;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_mt6991_disp_dd_ffff0000be0c2130,0x609,
               s_dp_handle____NULL_ffff0000be0b463c);
  }
  puVar2 = (undefined4 *)FUN_ffff0000be0443c4(*(undefined4 *)(param_1 + 0xe0));
  uVar1 = FUN_ffff0000be0440c4(*(undefined4 *)(param_1 + 0xe0));
  uVar4 = (ulong)uVar1;
  FUN_ffff0000be044938(*(undefined4 *)(param_1 + 0xe0));
  FUN_ffff0000be044e00
            (*(undefined4 *)(param_1 + 8),*(undefined4 *)(param_1 + 0xe0),
             *(undefined4 *)(param_1 + 0x10));
  if ((int)uVar1 < 1) {
    FUN_ffff0000be03f0e8(0x2f);
    FUN_ffff0000be03f0e8(0x30);
    FUN_ffff0000be03f0e8(0x4c);
    FUN_ffff0000be03f0e8(0x4d);
  }
  else {
    uVar5 = 0xffffffd5;
    plVar6 = (long *)PTR_DAT_ffff0000be0f5660;
    uVar7 = uVar4;
    puVar8 = puVar2;
    do {
      FUN_ffff0000be03f0e8(*puVar8);
      if (((uVar5 < 3) && (*plVar6 != 0)) &&
         (pcVar3 = *(code **)(*plVar6 + 0x78), pcVar3 != (code *)0x0)) {
        (*pcVar3)(uVar5 + 0x2b,1);
      }
      uVar5 = uVar5 + 1;
      plVar6 = plVar6 + 1;
      uVar7 = uVar7 - 1;
      puVar8 = puVar8 + 1;
    } while (uVar7 != 0);
    FUN_ffff0000be03f0e8(0x2f);
    FUN_ffff0000be03f0e8(0x30);
    FUN_ffff0000be03f0e8(0x4c);
    FUN_ffff0000be03f0e8(0x4d);
    if (0 < (int)uVar1) {
      do {
        FUN_ffff0000be03f03c(*puVar2);
        uVar4 = uVar4 - 1;
        puVar2 = puVar2 + 1;
      } while (uVar4 != 0);
    }
  }
  FUN_ffff0000be03f03c(0x2f);
  FUN_ffff0000be03f03c(0x30);
  FUN_ffff0000be03f03c(0x4c);
  FUN_ffff0000be03f03c(0x4d);
  return 0;
}


