
void FUN_ffff0000b803b5d4(long param_1)

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
    if (((((((iVar1 == DAT_ffff0000b80f03c0) || (iVar1 == DAT_ffff0000b80f0428)) ||
           (iVar1 == DAT_ffff0000b80f0490)) ||
          ((iVar1 == DAT_ffff0000b80f04f8 || (iVar1 == DAT_ffff0000b80f0560)))) ||
         (iVar1 == DAT_ffff0000b80f05c8)) ||
        (((((iVar1 == DAT_ffff0000b80f0630 || (iVar1 == DAT_ffff0000b80f0698)) ||
           ((iVar1 == DAT_ffff0000b80f0700 ||
            (((iVar1 == DAT_ffff0000b80f0768 || (iVar1 == DAT_ffff0000b80f07d0)) ||
             (iVar1 == DAT_ffff0000b80f0838)))))) ||
          (((iVar1 == DAT_ffff0000b80f08a0 || (iVar1 == DAT_ffff0000b80f0908)) ||
           ((iVar1 == DAT_ffff0000b80f0970 ||
            ((((iVar1 == DAT_ffff0000b80f09d8 || (iVar1 == DAT_ffff0000b80f0a40)) ||
              ((iVar1 == DAT_ffff0000b80f0aa8 ||
               (((iVar1 == DAT_ffff0000b80f0b10 || (iVar1 == DAT_ffff0000b80f0b78)) ||
                (iVar1 == DAT_ffff0000b80f0be0)))))) ||
             ((iVar1 == DAT_ffff0000b80f0c48 || (iVar1 == DAT_ffff0000b80f0cb0)))))))))) ||
         (iVar1 == DAT_ffff0000b80f0d18)))) ||
       (((iVar1 == DAT_ffff0000b80f0d80 || (iVar1 == DAT_ffff0000b80f0de8)) ||
        (iVar1 == DAT_ffff0000b80f0e50)))) {
      do {
        uVar4 = uVar2;
        uVar2 = uVar4 + 1;
      } while (*(int *)(PTR_DAT_ffff0000b8108758 + (long)*(int *)(param_1 + uVar4 * 4) * 0x20 + 0x10
                       ) == 0 && uVar4 < (uVar3 & 0xffffffff));
      if ((uVar3 & 0xffffffff) <= uVar4) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000b804b830
                  (s_platform_mediatek_mt6991_disp_dd_ffff0000b808caf3,0x5da,
                   s_step_<_module_num_ffff0000b8088690);
      }
    }
    uVar5 = uVar5 + 1;
    uVar6 = uVar6 + 1;
    if (uVar5 == (int)uVar3 - 1) {
      return;
    }
  } while( true );
}


