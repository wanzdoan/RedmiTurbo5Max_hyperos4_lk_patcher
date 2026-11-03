// ffff000090794724  size=328  FUN_ffff000090794724

int FUN_ffff000090794724(void)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  undefined4 uVar4;
  
  lVar2 = FUN_ffff000090792e88(0);
  FUN_ffff00009078f9bc(0,1);
  FUN_ffff0000907175bc(lVar2 + 0x270);
  iVar1 = FUN_ffff00009079119c();
  if (iVar1 == 0) {
    iVar1 = FUN_ffff000090793860(lVar2,0,0,lVar2 + 0x260);
    if (iVar1 == 0) {
      uVar4 = 1;
      iVar1 = FUN_ffff000090793860(lVar2,1,0,lVar2 + 0x264);
      if (iVar1 == 0) {
        uVar4 = 2;
        iVar1 = FUN_ffff000090793860(lVar2,2,0,lVar2 + 0x268);
        if (iVar1 == 0) {
          FUN_ffff00009079486c(s_bootdevice_ffff0000907d0ccc,3,*(undefined4 *)(lVar2 + 0x268),lVar2)
          ;
          FUN_ffff00009079486c
                    (s_preloader_a_ffff0000907d7a1f,1,*(undefined4 *)(lVar2 + 0x260),lVar2);
          FUN_ffff00009079486c
                    (s_preloader_b_ffff0000907d9251,2,*(undefined4 *)(lVar2 + 0x264),lVar2);
          FUN_ffff000090794f50();
          FUN_ffff00009077de54(s__UFS__info__boot_device_found_ffff00009080304f);
          pcVar3 = s__UFS__info__ufs_init_OK_ffff0000907fc5ee;
          goto LAB_ffff00009079476c;
        }
      }
    }
    else {
      uVar4 = 0;
    }
    FUN_ffff00009077de54(s__UFS__err__ufs_get_lu_size__d__f_ffff0000907fa9a8,uVar4,iVar1);
  }
  else {
    pcVar3 = s__UFS__err__ufshcd_init_failed_ffff0000907d9f1a;
LAB_ffff00009079476c:
    FUN_ffff00009077de54(pcVar3);
  }
  return iVar1;
}


