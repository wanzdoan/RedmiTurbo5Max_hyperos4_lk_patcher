
int FUN_ffff0000b8038730(long param_1,ulong param_2)

{
  uint uVar1;
  int iVar2;
  ulong uVar3;
  code *pcVar4;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_mt6991_disp_dd_ffff0000b809dc74,0x639,
               s_dp_handle____NULL_ffff0000b8092b68);
  }
  if (*(int *)(param_1 + (param_2 & 0xffffffff) * 0x10 + 0x40) == 0) {
    iVar2 = -1;
  }
  else {
    uVar1 = *(uint *)(param_1 + (param_2 & 0xffffffff) * 4 + 0x14);
    uVar3 = (ulong)(uVar1 >> 0x10);
    if ((*(long *)(PTR_DAT_ffff0000b8108760 + uVar3 * 8) == 0) ||
       (pcVar4 = *(code **)(*(long *)(PTR_DAT_ffff0000b8108760 + uVar3 * 8) + 0xa0),
       pcVar4 == (code *)0x0)) {
      iVar2 = -1;
    }
    else {
      iVar2 = (*pcVar4)(uVar3,uVar1 & 0xffff);
      if (0 < iVar2) {
        return iVar2;
      }
    }
    FUN_ffff0000b80385c4(param_1);
  }
  return iVar2;
}


