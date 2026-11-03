
int FUN_ffff0000be072368(void)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  undefined4 uVar4;
  
  lVar2 = FUN_ffff0000be070acc(0);
  FUN_ffff0000be06d79c(0,1);
  FUN_ffff0000be01a6f8(lVar2 + 0x270);
  iVar1 = FUN_ffff0000be06eefc();
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000be0714a4(lVar2,0,0,lVar2 + 0x260);
    if (iVar1 == 0) {
      uVar4 = 1;
      iVar1 = FUN_ffff0000be0714a4(lVar2,1,0,lVar2 + 0x264);
      if (iVar1 == 0) {
        uVar4 = 2;
        iVar1 = FUN_ffff0000be0714a4(lVar2,2,0,lVar2 + 0x268);
        if (iVar1 == 0) {
          FUN_ffff0000be0724b0(s_bootdevice_ffff0000be0a45a8,3,*(undefined4 *)(lVar2 + 0x268),lVar2)
          ;
          FUN_ffff0000be0724b0
                    (s_preloader_a_ffff0000be0a9083,1,*(undefined4 *)(lVar2 + 0x260),lVar2);
          FUN_ffff0000be0724b0
                    (s_preloader_b_ffff0000be0aae3c,2,*(undefined4 *)(lVar2 + 0x264),lVar2);
          FUN_ffff0000be072a08();
          FUN_ffff0000be05fd98(s__UFS__info__boot_device_found_ffff0000be0c6b7c);
          pcVar3 = s__UFS__info__ufs_init_OK_ffff0000be0c2599;
          goto LAB_ffff0000be0723b0;
        }
      }
    }
    else {
      uVar4 = 0;
    }
    FUN_ffff0000be05fd98(s__UFS__err__ufs_get_lu_size__d__f_ffff0000be0c141d,uVar4,iVar1);
  }
  else {
    pcVar3 = s__UFS__err__ufshcd_init_failed_ffff0000be0aae1d;
LAB_ffff0000be0723b0:
    FUN_ffff0000be05fd98(pcVar3);
  }
  return iVar1;
}


