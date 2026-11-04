
void FUN_ffff000090713fb4(void)

{
  ulong uVar1;
  char cVar2;
  int iVar3;
  int iVar4;
  uint uVar5;
  undefined4 uVar6;
  int iVar7;
  undefined4 uVar8;
  long lVar9;
  ulong uVar10;
  ulong *puVar11;
  undefined8 uVar12;
  long lVar13;
  char *pcVar14;
  ulong uVar15;
  ulong uVar16;
  ulong local_48;

  FUN_ffff00009071f2b0();
  lVar9 = FUN_ffff00009077fb34();
  cVar2 = FUN_ffff000090710708();
  iVar3 = FUN_ffff00009071617c();
  if ((cVar2 == '\x02') && (lVar9 == 0)) {
    iVar4 = FUN_ffff00009076935c(1);
    if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077d934
                (s_app_mt_boot_mt_boot_c_ffff0000907f2155,0x372,s_ret_>__0_ffff0000907d2a07);
    }
    pcVar14 = s_load_recovery_image_ffff0000907ece9c;
  }
  else {
    iVar4 = FUN_ffff00009076935c(0);
    if (iVar4 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077d934
                (s_app_mt_boot_mt_boot_c_ffff0000907f2155,0x377,s_ret_>__0_ffff0000907d2a07);
    }
    pcVar14 = s_load_boot_image_ffff0000907d2a10;
  }
  iVar4 = FUN_ffff00009071617c();
  FUN_ffff00009077ea84(s__PROFILE_______s_takes__d_ms_ffff0000907d0eee,pcVar14,iVar4 - iVar3);
  FUN_ffff00009070fb2c();
  FUN_ffff000090718ab8();
  uVar5 = FUN_ffff00009070fa34();
  if (2 < uVar5) {
    FUN_ffff00009070fb88();
    FUN_ffff000090718ab8();
  }
  iVar3 = FUN_ffff00009070fad0();
  uVar10 = FUN_ffff00009070fd28();
  uVar15 = uVar10 & 0xffffffff;
  lVar9 = FUN_ffff00009070fd84();
  iVar4 = FUN_ffff000090710494();
  uVar6 = FUN_ffff00009071052c();
  if (iVar3 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077d934
              (s_app_mt_boot_mt_boot_c_ffff0000907f2155,0x391,
               s_kernel_target_addr____0_ffff0000907cc085);
  }
  if ((int)uVar10 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077d934
              (s_app_mt_boot_mt_boot_c_ffff0000907f2155,0x392,
               s_ramdisk_target_addr____0_ffff0000907faf1d);
  }
  if (lVar9 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077d934
              (s_app_mt_boot_mt_boot_c_ffff0000907f2155,0x393,s_ramdisk_addr____0_ffff0000907d1171);
  }
  local_48 = 0;
  if (iVar4 != 0) goto LAB_ffff0000907140b0;
  while( true ) {
    FUN_ffff0000907128d0(iVar3,uVar6);
LAB_ffff0000907140b0:
    puVar11 = (ulong *)FUN_ffff000090719d88(s_mb_ramdisk_ffff0000907e678b,0);
    if (puVar11 == (ulong *)0x0) {
      FUN_ffff00009077ea84
                (s__s__d__reserved_memory_mb_ramdis_ffff0000907d4441,
                 s_boot_linux_from_storage_ffff0000907ca751,0x39f);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077d934(s_app_mt_boot_mt_boot_c_ffff0000907f2155,0x3a0,&DAT_ffff0000907ca5f8);
    }
    uVar1 = *puVar11;
    uVar16 = (ulong)(iVar4 + 0x800);
    uVar10 = uVar1 - 0x1000000000000;
    local_48 = uVar10;
    if (puVar11[1] < uVar16) {
      FUN_ffff00009077ea84
                (s__s__d__ramdisk_map_sz__0x_x__can_ffff0000907e9985,
                 s_boot_linux_from_storage_ffff0000907ca751,0x3a6,uVar16);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077d934(s_app_mt_boot_mt_boot_c_ffff0000907f2155,0x3a7,&DAT_ffff0000907ca5f8);
    }
    if (uVar1 != (uVar15 & 0xffffffff)) {
      FUN_ffff00009077ea84
                (s__s__d__ramdisk_addr__0x_x__is_no_ffff0000907c8bc7,
                 s_boot_linux_from_storage_ffff0000907ca751,0x3ac,uVar15 & 0xffffffff,uVar1);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077d934(s_app_mt_boot_mt_boot_c_ffff0000907f2155,0x3ad,&DAT_ffff0000907ca5f8);
    }
    lVar9 = FUN_ffff00009077bbb4(uVar10);
    if ((lVar9 == 0) &&
       (iVar7 = FUN_ffff00009077c2e4
                          (PTR_DAT_ffff000090831678,s_vm_ramdisk_ffff0000907d5e55,
                           uVar16 + 0xfff & 0x1fffff000,&local_48,0xc,uVar1,1,0), uVar10 = local_48,
       iVar7 != 0)) break;
    uVar15 = *puVar11;
    if (uVar10 != uVar15 - 0x1000000000000) {
      iVar7 = 0;
LAB_ffff0000907142f8:
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077d848
                (s_err__d__vm_ramdisk__0x_p__expect_ffff0000907f93aa,iVar7,uVar10,
                 uVar15 - 0x1000000000000);
    }
    uVar5 = FUN_ffff00009070fa34();
    uVar15 = local_48;
    if (2 < uVar5) {
      uVar12 = FUN_ffff00009070fee4(1);
      uVar8 = FUN_ffff000090710130(1);
      FUN_ffff00009077f488(uVar15,uVar12,uVar8);
      uVar15 = FUN_ffff000090710130(1);
      local_48 = local_48 + (uVar15 & 0xffffffff);
      cVar2 = FUN_ffff000090710708();
      if (cVar2 == '\x02') {
        lVar9 = FUN_ffff00009070fee4(2);
        lVar13 = FUN_ffff00009070fe58();
        uVar15 = local_48;
        if (lVar9 != lVar13) {
          uVar12 = FUN_ffff00009070fee4(2);
          uVar8 = FUN_ffff000090710130(2);
          FUN_ffff00009077f488(uVar15,uVar12,uVar8);
          FUN_ffff00009077ea84(s_Load_recovery_type_ramdisk_to_0x_ffff0000907dc9b3,local_48);
          uVar15 = FUN_ffff000090710130(2);
          local_48 = local_48 + (uVar15 & 0xffffffff);
        }
      }
    }
    uVar15 = local_48;
    uVar12 = FUN_ffff00009070fd84();
    uVar8 = FUN_ffff000090710360();
    FUN_ffff00009077f488(uVar15,uVar12,uVar8);
    uVar5 = FUN_ffff00009070fa34();
    if (3 < uVar5) {
      uVar15 = FUN_ffff000090710198();
      uVar8 = FUN_ffff0000907102e0();
      FUN_ffff00009071133c(uVar15,uVar8);
    }
  }
  uVar15 = *puVar11;
  goto LAB_ffff0000907142f8;
}
