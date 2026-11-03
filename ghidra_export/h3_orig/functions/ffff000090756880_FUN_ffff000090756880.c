// ffff000090756880  size=160  FUN_ffff000090756880

int FUN_ffff000090756880(long param_1,ulong param_2)

{
  uint uVar1;
  int iVar2;
  ulong uVar3;
  long lVar4;
  code *pcVar5;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_disp_dd_ffff0000907fbe99,0x639,
               s_dp_handle____NULL_ffff0000907e805e);
  }
  if (*(int *)(param_1 + (param_2 & 0xffffffff) * 0x10 + 0x40) == 0) {
    iVar2 = -1;
  }
  else {
    uVar1 = *(uint *)(param_1 + (param_2 & 0xffffffff) * 4 + 0x14);
    uVar3 = (ulong)(uVar1 >> 0x10);
    lVar4 = *(long *)(DAT_ffff0000908399a8 + uVar3 * 8);
    if ((lVar4 == 0) || (pcVar5 = *(code **)(lVar4 + 0xa0), pcVar5 == (code *)0x0)) {
      iVar2 = -1;
    }
    else {
      iVar2 = (*pcVar5)(uVar3,uVar1 & 0xffff);
      if (0 < iVar2) {
        return iVar2;
      }
    }
    FUN_ffff000090756714(param_1);
  }
  return iVar2;
}


