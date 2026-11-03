// ffff000090712ff4  size=2104  FUN_ffff000090712ff4

void FUN_ffff000090712ff4(ulong param_1,long param_2,undefined8 param_3,long param_4,uint param_5)

{
  ulong uVar1;
  char cVar2;
  undefined1 uVar3;
  uint uVar4;
  int iVar5;
  uint uVar6;
  undefined4 uVar7;
  int iVar8;
  int iVar9;
  long lVar10;
  long lVar11;
  long *plVar12;
  long lVar13;
  ulong uVar14;
  uint *puVar15;
  ulong *puVar16;
  undefined8 *puVar17;
  long lVar18;
  ulong uVar19;
  uint *puVar20;
  uint *puVar21;
  uint *local_90;
  undefined4 local_84;
  long local_80;
  long local_78;
  long local_70;
  uint local_64;
  
  local_78 = 0;
  local_70 = 0;
  local_80 = 0;
  local_84 = 0;
  local_90 = (uint *)0x0;
  lVar10 = FUN_ffff000090710024();
  uVar4 = FUN_ffff000090710124();
  uVar19 = (ulong)uVar4;
  FUN_ffff00009077de54
            (s__s__d__kernel_sz_0x_08x_ffff0000907fe93b,s_boot_linux_fdt_ffff0000907f0c9b,0x210,
             uVar19);
  iVar5 = FUN_ffff00009070fe74();
  if (iVar5 - 3U < 2) {
    lVar11 = FUN_ffff000090710298();
    iVar5 = FUN_ffff0000907106b0();
    lVar18 = 0;
    puVar21 = (uint *)(lVar11 + iVar5);
    uVar6 = FUN_ffff00009070fe14();
  }
  else {
    if (iVar5 != 2) {
      FUN_ffff00009077de54
                (s__s__d__64_bits_kernel_ffff0000907e8ce2,s_boot_linux_fdt_ffff0000907f0c9b,0x23a);
      if ((param_1 & 0x7ffff) != 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cc18(s_64_bit_kernel_can_t_boot_at_0x_0_ffff0000907e745a,param_1);
      }
      puVar20 = (uint *)(lVar10 + uVar19 + -4);
      iVar5 = 0xbfffc;
      puVar21 = puVar20;
      do {
        if ((((*(char *)((long)puVar20 + 3) == -0x13) && (*(char *)((long)puVar20 + 2) == -2)) &&
            (*(char *)((long)puVar20 + 1) == '\r')) && ((char)*puVar20 == -0x30))
        goto LAB_ffff0000907131c4;
        iVar5 = iVar5 + -1;
        puVar20 = (uint *)((long)puVar20 + -1);
        puVar21 = (uint *)((long)puVar21 + -1);
      } while (iVar5 != 0);
      FUN_ffff00009077de54
                (s__s__d__Can_t_find_main_device_tr_ffff0000907d8c9e,
                 s_boot_linux_fdt_ffff0000907f0c9b,0x24e);
      puVar21 = (uint *)0x0;
      puVar20 = (uint *)(lVar10 + uVar19 + -0xc0000);
LAB_ffff0000907131c4:
      uVar6 = (puVar20[1] & 0xff00ff00) >> 8 | (puVar20[1] & 0xff00ff) << 8;
      uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
      uVar19 = (ulong)(uVar4 - uVar6);
      FUN_ffff00009077de54
                (s__s__d__dtb_addr_0x_08lx__dtb_siz_ffff0000907d746b,
                 s_boot_linux_fdt_ffff0000907f0c9b,0x265,puVar21,uVar6);
      FUN_ffff00009077de54
                (s__s__d__kernel_load_addr_0x_08lx__ffff0000907da541,
                 s_boot_linux_fdt_ffff0000907f0c9b,0x266,lVar10,uVar19);
      lVar18 = 0;
      goto LAB_ffff000090713214;
    }
    iVar5 = FUN_ffff0000907100b0();
    iVar9 = FUN_ffff000090710820();
    iVar8 = FUN_ffff00009070feb8();
    puVar21 = (uint *)(lVar10 + iVar5 + (long)iVar9 + (long)iVar8);
    iVar5 = FUN_ffff0000907100b0();
    iVar9 = FUN_ffff000090710820();
    lVar18 = lVar10 + iVar5 + (long)iVar9;
    uVar6 = FUN_ffff00009070fe14();
  }
  if (uVar6 == 0) {
    uVar7 = FUN_ffff00009070fe74();
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cc18(s_can_t_find_v_d_dtb_ffff0000907e4212,uVar7);
  }
  uVar4 = (*puVar21 & 0xff00ff00) >> 8 | (*puVar21 & 0xff00ff) << 8;
  if ((uVar4 >> 0x10 | uVar4 << 0x10) == 0xd7b7ab1e) {
    uVar4 = (puVar21[4] & 0xff00ff00) >> 8 | (puVar21[4] & 0xff00ff) << 8;
    uVar4 = (uVar4 >> 0x10 | uVar4 << 0x10) * 0x20 + 0x20;
    puVar21 = (uint *)((long)puVar21 + (ulong)uVar4);
    uVar6 = uVar6 - uVar4;
  }
LAB_ffff000090713214:
  plVar12 = (long *)FUN_ffff00009071a204(s_mb_dtb_ffff0000907ebe29,0);
  if (plVar12 == (long *)0x0) {
    FUN_ffff00009077de54
              (s__s__d__reserved_memory_mb_dtb_do_ffff0000907e0b0d,s_boot_linux_fdt_ffff0000907f0c9b
               ,0x26c);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04(s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0x26d,&DAT_ffff0000907d24c9);
  }
  lVar11 = *plVar12;
  uVar1 = plVar12[1];
  local_70 = lVar11 + -0x1000000000000;
  if (lVar11 != param_2) {
    FUN_ffff00009077de54
              (s__s__d__fdt__0x_p__is_not_taken_f_ffff0000907dd939,s_boot_linux_fdt_ffff0000907f0c9b
               ,0x274,param_2,lVar11);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04(s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0x275,&DAT_ffff0000907d24c9);
  }
  lVar13 = FUN_ffff00009077b06c();
  if ((lVar13 == 0) &&
     (iVar5 = FUN_ffff00009077b79c
                        (DAT_ffff000090839908,s_vm_dtb_ffff000090801dfc,uVar1,&local_70,0xc,lVar11,1
                         ,0), iVar5 != 0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cc18(s_err__d__vm_dtb__0x_p__ffff0000907d2622,iVar5,local_70);
  }
  local_90 = puVar21;
  uVar14 = FUN_ffff00009071f9e8();
  if (((uVar14 & 1) == 0) && (cVar2 = FUN_ffff000090710b48(), cVar2 == '\x02')) {
    iVar5 = FUN_ffff000090784fcc(lVar18,0,&local_80,&local_84);
  }
  else {
    iVar5 = FUN_ffff0000907851bc(&DAT_ffff0000907f24b9,&DAT_ffff0000907f24b9,0,&local_80,&local_84);
  }
  FUN_ffff00009077de54(s_dtbo_size__u_ffff0000907e5b1e,local_84);
  if (iVar5 == 0) {
    iVar5 = FUN_ffff000090784e78(puVar21,uVar6,local_80,local_84,&local_90);
    if (iVar5 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0x29b,s_err____NO_ERROR_ffff0000907eda64);
    }
  }
  else {
    uVar4 = (local_90[1] & 0xff00ff00) >> 8 | (local_90[1] & 0xff00ff) << 8;
    FUN_ffff00009077de54
              (s_load_dtbo_fail__fdt_size__d__ffff0000907e747f,uVar4 >> 0x10 | uVar4 << 0x10);
  }
  uVar4 = (local_90[1] & 0xff00ff00) >> 8 | (local_90[1] & 0xff00ff) << 8;
  puVar15 = (uint *)FUN_ffff000090721e48(local_90,uVar4 >> 0x10 | uVar4 << 0x10,1);
  puVar20 = local_90;
  if ((puVar15 != (uint *)0x0) && (puVar20 = puVar15, local_90 != puVar21)) {
    thunk_FUN_ffff0000907c2ad8();
  }
  local_90 = puVar20;
  iVar5 = FUN_ffff00009076e9a4(local_90,local_70,uVar1 & 0xffffffff);
  if (iVar5 == 0) {
    uVar4 = (*(uint *)(local_70 + 4) & 0xff00ff00) >> 8 | (*(uint *)(local_70 + 4) & 0xff00ff) << 8;
    FUN_ffff00009077de54
              (s_DT_overlay_ok__fdt_size__d__ffff0000907dd96a,uVar4 >> 0x10 | uVar4 << 0x10);
  }
  if (local_90 != puVar21) {
    thunk_FUN_ffff0000907c2ad8();
  }
  if (local_80 != 0) {
    thunk_FUN_ffff0000907c2ad8();
  }
  FUN_ffff000090712574(local_70);
  puVar16 = (ulong *)FUN_ffff00009071a204(s_mb_kernel_ffff0000907e4226,0);
  if (puVar16 == (ulong *)0x0) {
    FUN_ffff00009077de54
              (s__s__d__reserved_memory_mb_kernel_ffff0000907fb04f,s_boot_linux_fdt_ffff0000907f0c9b
               ,699);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04(s_app_mt_boot_mt_boot_c_ffff0000907f92d4,700,&DAT_ffff0000907d24c9);
  }
  uVar1 = puVar16[1];
  local_78 = *puVar16 - 0x1000000000000;
  if (*puVar16 != param_1) {
    FUN_ffff00009077de54
              (s__s__d__kernel_addr__0x_p__is_not_ffff0000908003a9,s_boot_linux_fdt_ffff0000907f0c9b
               ,0x2c3,param_1);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04(s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0x2c4,&DAT_ffff0000907d24c9);
  }
  lVar18 = FUN_ffff00009077b06c();
  if ((lVar18 == 0) &&
     (iVar5 = FUN_ffff00009077b79c
                        (DAT_ffff000090839908,s_vm_kernel_ffff0000907df41e,uVar1,&local_78,0xc,
                         param_1,1,0), iVar5 != 0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cc18(s_err__d__vm_kernel__0x_p__ffff0000907d2639,iVar5,local_78);
  }
  FUN_ffff00009077de54
            (s__s__d__decompress_kernel_image___ffff0000907f25a5,s_boot_linux_fdt_ffff0000907f0c9b,
             0x2da);
  iVar5 = FUN_ffff000090711f84(lVar10,local_78,uVar19,uVar1 & 0xffffffff);
  if (iVar5 != 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cc18(s_decompress_kernel_image_fail____ffff0000907d584b);
  }
  iVar5 = FUN_ffff0000907c56d4();
  if (iVar5 != 0) {
    FUN_ffff00009077de54(s__AVB__Failed_to_free_AVB_heap__e_ffff0000907e5b2c,iVar5);
  }
  FUN_ffff000090718f34(s_initcall_blacklist_ufshcd_core_i_ffff0000907dd987);
  FUN_ffff000090713840();
  FUN_ffff000090713930();
  FUN_ffff000090710f10();
  FUN_ffff00009071147c();
  FUN_ffff0000907116fc();
  FUN_ffff000090711380();
  FUN_ffff00009071253c();
  FUN_ffff0000907852c8(local_70);
  puVar17 = (undefined8 *)FUN_ffff00009071a204(s_BL33_reserved_ffff0000907fb080,0);
  if (puVar17 == (undefined8 *)0x0) {
    FUN_ffff00009077de54
              (s__s__d__reserved_memory_BL33_rese_ffff0000907fca75,s_boot_linux_fdt_ffff0000907f0c9b
               ,0x2fe);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04(s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0x2ff,&DAT_ffff0000907d24c9);
  }
  FUN_ffff00009071a6ac(*puVar17);
  FUN_ffff00009071a6ac(param_1);
  FUN_ffff00009071a6ac(lVar11);
  if (param_5 != 0) {
    FUN_ffff00009071a6ac(param_4);
  }
  FUN_ffff000090783d08();
  FUN_ffff0000907880ec();
  FUN_ffff00009071b0e0(local_70);
  iVar5 = FUN_ffff000090718a4c(local_70);
  if (iVar5 == 0) {
    iVar5 = FUN_ffff00009076d75c(local_70,s__chosen_ffff0000907ea59a);
    if (iVar5 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0x322,s_offset_>__0_ffff0000907f3f73);
    }
    uVar6 = (uint)param_4;
    uVar4 = (uVar6 & 0xff00ff00) >> 8 | (uVar6 & 0xff00ff) << 8;
    local_64 = uVar4 >> 0x10 | uVar4 << 0x10;
    iVar9 = FUN_ffff00009076e40c(local_70,iVar5,s_linux_initrd_start_ffff0000907da576,&local_64,4);
    if (iVar9 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0x325,s_err_>__0_ffff0000907ebe30);
    }
    iVar9 = FUN_ffff000090711b00(local_70,param_4 + (ulong)param_5 + -0x1000000000000);
    uVar4 = uVar6 + param_5 + iVar9;
    uVar4 = (uVar4 & 0xff00ff00) >> 8 | (uVar4 & 0xff00ff) << 8;
    local_64 = uVar4 >> 0x10 | uVar4 << 0x10;
    iVar5 = FUN_ffff00009076e40c(local_70,iVar5,s_linux_initrd_end_ffff0000907d8cc3,&local_64,4);
    if (iVar5 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0x32e,s_err_>__0_ffff0000907ebe30);
    }
    uVar3 = FUN_ffff000090710b48();
    iVar5 = FUN_ffff000090788938(uVar3);
    if (iVar5 != 0) {
      FUN_ffff00009077de54
                (s__s__d___SEC__Failed_to_update_OT_ffff0000907dbfe0,
                 s_boot_linux_fdt_ffff0000907f0c9b,0x332,iVar5);
    }
    iVar5 = FUN_ffff0000907167d4();
    FUN_ffff00009072a27c(local_70);
    iVar9 = FUN_ffff0000907167d4();
    FUN_ffff00009077de54
              (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_hyp_unmap2_fdt__ffff0000907fb08e,
               iVar9 - iVar5);
    FUN_ffff0000907143c4(local_70);
    iVar5 = FUN_ffff00009076ec98(local_70);
    if (iVar5 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_app_mt_boot_mt_boot_c_ffff0000907f92d4,0x342,s_err_>__0_ffff0000907ebe30);
    }
    uVar7 = FUN_ffff00009071382c(local_70 + 4);
    FUN_ffff00009077de54
              (s__s__d__booting_linux___0x_llx__r_ffff0000907d8cd4,s_boot_linux_fdt_ffff0000907f0c9b
               ,0x346,param_1,param_4,param_5,lVar11,uVar7);
    uVar3 = FUN_ffff000090710b48();
    FUN_ffff00009077de54
              (s__s__d__lk_boot_mode____d_ffff0000907e749d,s_boot_linux_fdt_ffff0000907f0c9b,0x347,
               uVar3);
    FUN_ffff00009077de54
              (s__s__d__lk_finished___>_jump_to_l_ffff0000907f4004,s_boot_linux_fdt_ffff0000907f0c9b
               ,0x349,s_64Bit_ffff0000907fb09e);
    FUN_ffff000090764490();
    FUN_ffff000090764ae4();
    FUN_ffff000090764ff4();
    FUN_ffff00009071ea80(FUN_ffff0000907145bc,0xc2000115,param_1,lVar11,0);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_app_mt_boot_mt_boot_c_ffff0000907f92d4,799,s_err____NO_ERROR_ffff0000907eda64);
}


