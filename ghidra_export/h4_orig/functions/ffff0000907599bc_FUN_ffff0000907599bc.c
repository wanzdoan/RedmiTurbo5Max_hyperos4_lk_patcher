// ffff0000907599bc  size=1128  FUN_ffff0000907599bc

void FUN_ffff0000907599bc(long param_1)

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
    if (((((((iVar1 == DAT_ffff000090820b70) || (iVar1 == DAT_ffff000090820bd8)) ||
           (iVar1 == DAT_ffff000090820c40)) ||
          ((iVar1 == DAT_ffff000090820ca8 || (iVar1 == DAT_ffff000090820d10)))) ||
         (iVar1 == DAT_ffff000090820d78)) ||
        (((((iVar1 == DAT_ffff000090820de0 || (iVar1 == DAT_ffff000090820e48)) ||
           ((iVar1 == DAT_ffff000090820eb0 ||
            (((iVar1 == DAT_ffff000090820f18 || (iVar1 == DAT_ffff000090820f80)) ||
             (iVar1 == DAT_ffff000090820fe8)))))) ||
          (((iVar1 == DAT_ffff000090821050 || (iVar1 == DAT_ffff0000908210b8)) ||
           ((iVar1 == DAT_ffff000090821120 ||
            ((((iVar1 == DAT_ffff000090821188 || (iVar1 == DAT_ffff0000908211f0)) ||
              ((iVar1 == DAT_ffff000090821258 ||
               (((iVar1 == DAT_ffff0000908212c0 || (iVar1 == DAT_ffff000090821328)) ||
                (iVar1 == DAT_ffff000090821390)))))) ||
             ((iVar1 == DAT_ffff0000908213f8 || (iVar1 == DAT_ffff000090821460)))))))))) ||
         (iVar1 == DAT_ffff0000908214c8)))) ||
       (((iVar1 == DAT_ffff000090821530 || (iVar1 == DAT_ffff000090821598)) ||
        (iVar1 == DAT_ffff000090821600)))) {
      do {
        uVar4 = uVar2;
        uVar2 = uVar4 + 1;
      } while (*(int *)(PTR_DAT_ffff0000908399c0 + (long)*(int *)(param_1 + uVar4 * 4) * 0x20 + 0x10
                       ) == 0 && uVar4 < (uVar3 & 0xffffffff));
      if ((uVar3 & 0xffffffff) <= uVar4) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_mt6991_disp_dd_ffff0000907dce7e,0x5da,
                   s_step_<_module_num_ffff0000907d4cf0);
      }
    }
    uVar5 = uVar5 + 1;
    uVar6 = uVar6 + 1;
    if (uVar5 == (int)uVar3 - 1) {
      return;
    }
  } while( true );
}


