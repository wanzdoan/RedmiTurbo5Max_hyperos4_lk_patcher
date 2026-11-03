// ffff000090759724  size=1128  FUN_ffff000090759724

void FUN_ffff000090759724(long param_1)

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
    if (((((((iVar1 == DAT_ffff000090820b50) || (iVar1 == DAT_ffff000090820bb8)) ||
           (iVar1 == DAT_ffff000090820c20)) ||
          ((iVar1 == DAT_ffff000090820c88 || (iVar1 == DAT_ffff000090820cf0)))) ||
         (iVar1 == DAT_ffff000090820d58)) ||
        (((((iVar1 == DAT_ffff000090820dc0 || (iVar1 == DAT_ffff000090820e28)) ||
           ((iVar1 == DAT_ffff000090820e90 ||
            (((iVar1 == DAT_ffff000090820ef8 || (iVar1 == DAT_ffff000090820f60)) ||
             (iVar1 == DAT_ffff000090820fc8)))))) ||
          (((iVar1 == DAT_ffff000090821030 || (iVar1 == DAT_ffff000090821098)) ||
           ((iVar1 == DAT_ffff000090821100 ||
            ((((iVar1 == DAT_ffff000090821168 || (iVar1 == DAT_ffff0000908211d0)) ||
              ((iVar1 == DAT_ffff000090821238 ||
               (((iVar1 == DAT_ffff0000908212a0 || (iVar1 == DAT_ffff000090821308)) ||
                (iVar1 == DAT_ffff000090821370)))))) ||
             ((iVar1 == DAT_ffff0000908213d8 || (iVar1 == DAT_ffff000090821440)))))))))) ||
         (iVar1 == DAT_ffff0000908214a8)))) ||
       (((iVar1 == DAT_ffff000090821510 || (iVar1 == DAT_ffff000090821578)) ||
        (iVar1 == DAT_ffff0000908215e0)))) {
      do {
        uVar4 = uVar2;
        uVar2 = uVar4 + 1;
      } while (*(int *)(PTR_DAT_ffff0000908399a0 + (long)*(int *)(param_1 + uVar4 * 4) * 0x20 + 0x10
                       ) == 0 && uVar4 < (uVar3 & 0xffffffff));
      if ((uVar3 & 0xffffffff) <= uVar4) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_mt6991_disp_dd_ffff0000907dcd91,0x5da,
                   s_step_<_module_num_ffff0000907d4cb3);
      }
    }
    uVar5 = uVar5 + 1;
    uVar6 = uVar6 + 1;
    if (uVar5 == (int)uVar3 - 1) {
      return;
    }
  } while( true );
}


