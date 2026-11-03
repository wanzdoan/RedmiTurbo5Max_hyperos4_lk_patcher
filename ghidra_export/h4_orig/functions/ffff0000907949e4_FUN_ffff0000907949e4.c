// ffff0000907949e4  size=328  FUN_ffff0000907949e4

int FUN_ffff0000907949e4(void)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  undefined4 uVar4;
  
  lVar2 = FUN_ffff000090793148(0);
  FUN_ffff00009078fc7c(0,1);
  FUN_ffff000090717700(lVar2 + 0x270);
  iVar1 = FUN_ffff00009079145c();
  if (iVar1 == 0) {
    iVar1 = FUN_ffff000090793b20(lVar2,0,0,lVar2 + 0x260);
    if (iVar1 == 0) {
      uVar4 = 1;
      iVar1 = FUN_ffff000090793b20(lVar2,1,0,lVar2 + 0x264);
      if (iVar1 == 0) {
        uVar4 = 2;
        iVar1 = FUN_ffff000090793b20(lVar2,2,0,lVar2 + 0x268);
        if (iVar1 == 0) {
          FUN_ffff000090794b2c(s_bootdevice_ffff0000907d0d09,3,*(undefined4 *)(lVar2 + 0x268),lVar2)
          ;
          FUN_ffff000090794b2c
                    (s_preloader_a_ffff0000907d7ab5,1,*(undefined4 *)(lVar2 + 0x260),lVar2);
          FUN_ffff000090794b2c
                    (s_preloader_b_ffff0000907d92e8,2,*(undefined4 *)(lVar2 + 0x264),lVar2);
          FUN_ffff000090795210();
          FUN_ffff00009077e11c(s__UFS__info__boot_device_found_ffff000090803253);
          pcVar3 = s__UFS__info__ufs_init_OK_ffff0000907fc78a;
          goto LAB_ffff000090794a2c;
        }
      }
    }
    else {
      uVar4 = 0;
    }
    FUN_ffff00009077e11c(s__UFS__err__ufs_get_lu_size__d__f_ffff0000907fab44,uVar4,iVar1);
  }
  else {
    pcVar3 = s__UFS__err__ufshcd_init_failed_ffff0000907d9fc8;
LAB_ffff000090794a2c:
    FUN_ffff00009077e11c(pcVar3);
  }
  return iVar1;
}


