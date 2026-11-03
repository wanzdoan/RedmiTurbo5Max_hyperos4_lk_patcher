
void FUN_ffff0000be044998(long param_1)

{
  int iVar1;
  ulong uVar2;
  ulong uVar3;
  ulong uVar4;
  ulong uVar5;
  ulong uVar6;
  
  uVar3 = 0;
  do {
    if (*(int *)(param_1 + uVar3 * 4) == -1) {
      if ((uint)uVar3 < 2) {
        return;
      }
      break;
    }
    uVar3 = uVar3 + 1;
  } while (uVar3 != 0x4b);
  uVar5 = 0;
  uVar6 = 1;
  do {
    iVar1 = *(int *)(param_1 + uVar5 * 4);
    uVar2 = uVar6;
    if (((((((iVar1 == DAT_ffff0000be0dd490) || (iVar1 == DAT_ffff0000be0dd4f8)) ||
           (iVar1 == DAT_ffff0000be0dd560)) ||
          ((iVar1 == DAT_ffff0000be0dd5c8 || (iVar1 == DAT_ffff0000be0dd630)))) ||
         (iVar1 == DAT_ffff0000be0dd698)) ||
        (((((iVar1 == DAT_ffff0000be0dd700 || (iVar1 == DAT_ffff0000be0dd768)) ||
           ((iVar1 == DAT_ffff0000be0dd7d0 ||
            (((iVar1 == DAT_ffff0000be0dd838 || (iVar1 == DAT_ffff0000be0dd8a0)) ||
             (iVar1 == DAT_ffff0000be0dd908)))))) ||
          (((iVar1 == DAT_ffff0000be0dd970 || (iVar1 == DAT_ffff0000be0dd9d8)) ||
           ((iVar1 == DAT_ffff0000be0dda40 ||
            ((((iVar1 == DAT_ffff0000be0ddaa8 || (iVar1 == DAT_ffff0000be0ddb10)) ||
              ((iVar1 == DAT_ffff0000be0ddb78 ||
               (((iVar1 == DAT_ffff0000be0ddbe0 || (iVar1 == DAT_ffff0000be0ddc48)) ||
                (iVar1 == DAT_ffff0000be0ddcb0)))))) ||
             ((iVar1 == DAT_ffff0000be0ddd18 || (iVar1 == DAT_ffff0000be0ddd80)))))))))) ||
         (iVar1 == DAT_ffff0000be0ddde8)))) ||
       (((iVar1 == DAT_ffff0000be0dde50 || (iVar1 == DAT_ffff0000be0ddeb8)) ||
        (iVar1 == DAT_ffff0000be0ddf20)))) {
      do {
        uVar4 = uVar2;
        uVar2 = uVar4 + 1;
      } while (*(int *)(PTR_DAT_ffff0000be0f5658 + (long)*(int *)(param_1 + uVar4 * 4) * 0x20 + 0x10
                       ) == 0 && uVar4 < (uVar3 & 0xffffffff));
      if ((uVar3 & 0xffffffff) <= uVar4) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000be05ee98
                  (s_platform_mediatek_mt6991_disp_dd_ffff0000be0acca6,0x5da,
                   s_step_<_module_num_ffff0000be0a7360);
      }
    }
    uVar5 = uVar5 + 1;
    uVar6 = uVar6 + 1;
    if (uVar5 == (int)uVar3 - 1) {
      return;
    }
  } while( true );
}


