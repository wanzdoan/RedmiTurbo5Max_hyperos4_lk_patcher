
undefined8 FUN_ffff0000be041774(long param_1)

{
  undefined *puVar1;
  uint uVar2;
  int iVar3;
  long lVar4;
  code *pcVar5;
  ulong uVar6;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_mt6991_disp_dd_ffff0000be0c2130,0x59b,
               s_dp_handle____NULL_ffff0000be0b463c);
  }
  lVar4 = FUN_ffff0000be0443c4(*(undefined4 *)(param_1 + 0xe0));
  uVar2 = FUN_ffff0000be0440c4(*(undefined4 *)(param_1 + 0xe0));
  puVar1 = PTR_DAT_ffff0000be0f5660;
  if (0 < (int)uVar2) {
    uVar6 = (ulong)uVar2 + 1;
    do {
      if (((*(long *)(puVar1 + (ulong)*(uint *)(lVar4 + (ulong)((int)uVar6 - 2) * 4) * 8) != 0) &&
          (pcVar5 = *(code **)(*(long *)(puVar1 + (ulong)*(uint *)(lVar4 + (ulong)((int)uVar6 - 2) *
                                                                           4) * 8) + 0x70),
          pcVar5 != (code *)0x0)) && (iVar3 = (*pcVar5)(), iVar3 != 0)) {
        return 1;
      }
      uVar6 = uVar6 - 1;
    } while (1 < uVar6);
  }
  return 0;
}


