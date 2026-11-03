// File offset entry: 0x9174

void FUN_ffff0000b8009174(void)

{
  undefined8 uVar1;
  undefined8 uVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  int iVar6;
  long *plVar7;
  undefined8 *puVar8;
  undefined8 uVar9;
  long alStack_50 [2];
  long lStack_28;
  
  lStack_28 = 0x6b6c;
  uVar1 = *(undefined8 *)PTR_DAT_ffff0000b81075e8;
  uVar2 = *(undefined8 *)(PTR_DAT_ffff0000b81075e8 + 8);
  uVar9 = *(undefined8 *)(PTR_DAT_ffff0000b81075e8 + 0x10);
  iVar3 = FUN_ffff0000b800a01c();
  plVar7 = (long *)FUN_ffff0000b800db20(s_BL33_reserved_ffff0000b809c297,0);
  if (plVar7 == (long *)0x0) {
    FUN_ffff0000b804beb0(s_reserved_memory_BL33_reserved_do_ffff0000b8087957);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b3a4(s_app_bl2_ext_bl2_ext_c_ffff0000b8092fed,0xad,&DAT_ffff0000b8085ec6);
  }
  alStack_50[0] = *plVar7 + -0x1000000000000;
  iVar4 = FUN_ffff0000b804a16c
                    (PTR_DAT_ffff0000b8107610,s_vm_BL33_reserved_ffff0000b8094095,plVar7[1],
                     alStack_50,0xc,*plVar7,1,0);
  if (iVar4 != 0) {
    FUN_ffff0000b804beb0(s_BL33_vmm_alloc_physical_failed__e_ffff0000b809e151,iVar4);
LAB_ffff0000b8009274:
    FUN_ffff0000b800d264(*plVar7);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b3a4
              (s_app_bl2_ext_bl2_ext_c_ffff0000b8092fed,0xff,
               s_app_load_bl33______NO_ERROR_ffff0000b8085e5a);
  }
  iVar4 = FUN_ffff0000b8050a2c(&lStack_28,&DAT_ffff0000b809b089,alStack_50[0],plVar7[1]);
  if (iVar4 < 0) {
    FUN_ffff0000b804beb0(s_BL33_load_failed__ret___d_ffff0000b809e17a,iVar4);
    FUN_ffff0000b804a318(PTR_DAT_ffff0000b8107610,alStack_50[0]);
    goto LAB_ffff0000b8009274;
  }
  iVar4 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804beb0
            (s__PROFILE_______s_takes__d_ms_ffff0000b8089606,s_load_bl33_ffff0000b8098807,
             iVar4 - iVar3);
  alStack_50[0] = 0x656574;
  alStack_50[1] = 0;
  iVar3 = FUN_ffff0000b800a01c();
  plVar7 = (long *)FUN_ffff0000b800db20(s_BL31_reserved_ffff0000b809e143,0);
  if (plVar7 == (long *)0x0) {
    FUN_ffff0000b804beb0(s_reserved_memory_BL31_reserved_do_ffff0000b8097971);
    plVar7 = (long *)FUN_ffff0000b804b32c
                               (s_app_bl2_ext_bl2_ext_c_ffff0000b8092fed,0x75,&DAT_ffff0000b8085ec6,
                                s_LK_ATF_ffff0000b809d105);
  }
  lStack_28 = *plVar7 + -0x1000000000000;
  iVar4 = FUN_ffff0000b804a16c
                    (PTR_DAT_ffff0000b8107610,s_vm_BL31_reserved_ffff0000b8084f5f,plVar7[1],
                     &lStack_28,0xc,*plVar7,1,0);
  if (iVar4 == 0) {
    iVar4 = FUN_ffff0000b8050a2c(alStack_50,&DAT_ffff0000b809d10c,lStack_28,plVar7[1]);
    if (iVar4 < 0) {
      FUN_ffff0000b804beb0(s__BL31__load_failed__ret___d_ffff0000b809128f,iVar4);
      FUN_ffff0000b804a318(PTR_DAT_ffff0000b8107610,lStack_28);
      goto LAB_ffff0000b8009390;
    }
  }
  else {
    FUN_ffff0000b804beb0(s__BL31__vmm_alloc_physical_failed_ffff0000b808e7bd,iVar4);
LAB_ffff0000b8009390:
    FUN_ffff0000b800d264(*plVar7);
    FUN_ffff0000b804b32c
              (s_app_bl2_ext_bl2_ext_c_ffff0000b8092fed,0x100,
               s_app_load_bl31______NO_ERROR_ffff0000b809fd06,s_LK_ATF_ffff0000b809d105);
  }
  FUN_ffff0000b804a318(PTR_DAT_ffff0000b8107610,lStack_28);
  iVar4 = FUN_ffff0000b8028aa4();
  if (iVar4 != 0) {
    FUN_ffff0000b804beb0(s__BL31__Prepare_bl31_dtb_failed_r_ffff0000b809fd47,iVar4);
  }
  iVar4 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804beb0
            (s__PROFILE_______s_takes__d_ms_ffff0000b8089606,s_load_bl31_ffff0000b8098811,
             iVar4 - iVar3);
  iVar3 = FUN_ffff0000b8016fb8();
  if (iVar3 == 0) {
    iVar3 = FUN_ffff0000b800a01c();
    iVar4 = FUN_ffff0000b800e37c();
    FUN_ffff0000b801704c();
    iVar5 = FUN_ffff0000b800a01c();
    FUN_ffff0000b804beb0
              (s__PROFILE_______s_takes__d_ms_ffff0000b8089606,s_load_pvmfw_ffff0000b808e7e8,
               iVar5 - iVar3);
    if (iVar4 != 0) goto LAB_ffff0000b80095dc;
    iVar3 = FUN_ffff0000b800a01c();
    iVar4 = FUN_ffff0000b80266b8();
    iVar5 = FUN_ffff0000b800a01c();
    FUN_ffff0000b804beb0
              (s__PROFILE_______s_takes__d_ms_ffff0000b8089606,s_load_sspm_ffff0000b8086dec,
               iVar5 - iVar3);
    if (iVar4 != 0) goto LAB_ffff0000b80095fc;
    iVar3 = FUN_ffff0000b800a01c();
    iVar4 = FUN_ffff0000b801a430();
    iVar5 = FUN_ffff0000b800a01c();
    FUN_ffff0000b804beb0
              (s__PROFILE_______s_takes__d_ms_ffff0000b8089606,s_load_kraken_ffff0000b809e195,
               iVar5 - iVar3);
    if (iVar4 != 0) goto LAB_ffff0000b800961c;
    iVar3 = FUN_ffff0000b800a01c();
    iVar4 = FUN_ffff0000b801cff0();
    iVar5 = FUN_ffff0000b800a01c();
    FUN_ffff0000b804beb0
              (s__PROFILE_______s_takes__d_ms_ffff0000b8089606,s_load_mcupm_ffff0000b808d804,
               iVar5 - iVar3);
    if (iVar4 != 0) goto LAB_ffff0000b800963c;
    iVar3 = FUN_ffff0000b800a01c();
    iVar4 = FUN_ffff0000b8028c7c();
    iVar5 = FUN_ffff0000b8027b4c();
    iVar6 = FUN_ffff0000b800a01c();
    FUN_ffff0000b804beb0
              (s__PROFILE_______s_takes__d_ms_ffff0000b8089606,s_load_bl32_ffff0000b809b08c,
               iVar6 - iVar3);
    if (iVar5 != 0 || iVar4 != 0) goto LAB_ffff0000b800965c;
    iVar3 = FUN_ffff0000b800a01c();
    iVar4 = FUN_ffff0000b800e3b4();
    iVar5 = FUN_ffff0000b800a01c();
    FUN_ffff0000b804beb0
              (s__PROFILE_______s_takes__d_ms_ffff0000b8089606,s_load_picachu_ffff0000b8085ec8,
               iVar5 - iVar3);
    if (iVar4 == 0) {
      iVar3 = FUN_ffff0000b800a01c();
      iVar4 = FUN_ffff0000b800e36c();
      iVar5 = FUN_ffff0000b800a01c();
      FUN_ffff0000b804beb0
                (s__PROFILE_______s_takes__d_ms_ffff0000b8089606,s_load_ise_ffff0000b809b096,
                 iVar5 - iVar3);
      if (iVar4 != 0) goto LAB_ffff0000b800969c;
      puVar8 = (undefined8 *)FUN_ffff0000b800db20(s_BL31_reserved_ffff0000b809e143,0);
      if (puVar8 == (undefined8 *)0x0) {
        FUN_ffff0000b804beb0(s_reserved_memory_BL31_reserved_do_ffff0000b8097971);
        puVar8 = (undefined8 *)
                 FUN_ffff0000b804b32c
                           (s_app_bl2_ext_bl2_ext_c_ffff0000b8092fed,0x10d,&DAT_ffff0000b8085ec6,
                            s_LK_ATF_ffff0000b809d105);
      }
      FUN_ffff0000b800e6a0(0xffff0000b8009158,*puVar8,uVar1,uVar2,uVar9);
      goto LAB_ffff0000b80095bc;
    }
  }
  else {
LAB_ffff0000b80095bc:
    FUN_ffff0000b804b32c
              (s_app_bl2_ext_bl2_ext_c_ffff0000b8092fed,0x101,
               s_app_load_hypervisor______NO_ERRO_ffff0000b809ef6b,s_LK_GZ_ffff0000b8096c12);
LAB_ffff0000b80095dc:
    FUN_ffff0000b804b32c
              (s_app_bl2_ext_bl2_ext_c_ffff0000b8092fed,0x102,
               s_app_load_pvmfw______NO_ERROR_ffff0000b8085e76,s_LK_PVMFW_ffff0000b8085e93);
LAB_ffff0000b80095fc:
    FUN_ffff0000b804b32c
              (s_app_bl2_ext_bl2_ext_c_ffff0000b8092fed,0x103,
               s_app_load_sspm______NO_ERROR_ffff0000b8087934,s_LK_SSPM_ffff0000b809e13b);
LAB_ffff0000b800961c:
    FUN_ffff0000b804b32c
              (s_app_bl2_ext_bl2_ext_c_ffff0000b8092fed,0x104,
               s_app_load_kraken______NO_ERROR_ffff0000b8090450,s_LK_KRAKEN_ffff0000b809fd22);
LAB_ffff0000b800963c:
    FUN_ffff0000b804b32c
              (s_app_bl2_ext_bl2_ext_c_ffff0000b8092fed,0x105,
               s_app_load_mcupm______NO_ERROR_ffff0000b8093003,s_LK_MCUPM_ffff0000b809046e);
LAB_ffff0000b800965c:
    FUN_ffff0000b804b32c
              (s_app_bl2_ext_bl2_ext_c_ffff0000b8092fed,0x106,
               s_app_load_bl32______NO_ERROR_ffff0000b809a42a,s_LK_TEE_ffff0000b809a446);
  }
  FUN_ffff0000b804b32c
            (s_app_bl2_ext_bl2_ext_c_ffff0000b8092fed,0x107,
             s_app_load_picachu______NO_ERROR_ffff0000b8085e9c,s_LK_PICACHU_ffff0000b8085ebb);
LAB_ffff0000b800969c:
  FUN_ffff0000b804b32c
            (s_app_bl2_ext_bl2_ext_c_ffff0000b8092fed,0x108,
             s_app_load_ise______NO_ERROR_ffff0000b809fd2c,s_LK_ISE_ffff0000b8087950);
  return;
}


