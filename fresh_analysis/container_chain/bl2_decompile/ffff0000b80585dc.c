
int FUN_ffff0000b80585dc(void)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  undefined4 uVar4;
  
  lVar2 = FUN_ffff0000b8056e5c(0);
  FUN_ffff0000b8053a10(0,1);
  FUN_ffff0000b800ad58(lVar2 + 0x270);
  iVar1 = FUN_ffff0000b8055170();
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000b8057834(lVar2,0,0,lVar2 + 0x260);
    if (iVar1 == 0) {
      uVar4 = 1;
      iVar1 = FUN_ffff0000b8057834(lVar2,1,0,lVar2 + 0x264);
      if (iVar1 == 0) {
        uVar4 = 2;
        iVar1 = FUN_ffff0000b8057834(lVar2,2,0,lVar2 + 0x268);
        if (iVar1 == 0) {
          FUN_ffff0000b8058724(s_bootdevice_ffff0000b80863d5,3,*(undefined4 *)(lVar2 + 0x268),lVar2)
          ;
          FUN_ffff0000b8058724
                    (s_preloader_a_ffff0000b8089bb1,1,*(undefined4 *)(lVar2 + 0x260),lVar2);
          FUN_ffff0000b8058724
                    (s_preloader_b_ffff0000b808b194,2,*(undefined4 *)(lVar2 + 0x264),lVar2);
          FUN_ffff0000b8058ce4();
          FUN_ffff0000b804c33c(s__UFS__info__boot_device_found_ffff0000b80a16fc);
          pcVar3 = s__UFS__info__ufs_init_OK_ffff0000b809df6c;
          goto LAB_ffff0000b8058624;
        }
      }
    }
    else {
      uVar4 = 0;
    }
    FUN_ffff0000b804c33c(s__UFS__err__ufs_get_lu_size__d__f_ffff0000b809d07e,uVar4,iVar1);
  }
  else {
    pcVar3 = s__UFS__err__ufshcd_init_failed_ffff0000b808b175;
LAB_ffff0000b8058624:
    FUN_ffff0000b804c33c(pcVar3);
  }
  return iVar1;
}


