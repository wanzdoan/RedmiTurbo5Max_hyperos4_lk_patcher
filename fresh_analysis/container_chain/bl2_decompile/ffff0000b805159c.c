
int FUN_ffff0000b805159c(long *param_1)

{
  long *plVar1;
  char *pcVar2;
  undefined8 uVar3;
  bool bVar4;
  int iVar5;
  uint uVar6;
  ulong uVar7;
  long lVar8;
  undefined4 local_18;
  undefined2 local_14;
  
  local_14 = 0;
  local_18 = 0;
  uVar7 = FUN_ffff0000b8011a98();
  bVar4 = (uVar7 & 1) == 0;
  pcVar2 = s_logo2_ffff0000b8096aa5;
  if (bVar4) {
    pcVar2 = s_logo1_ffff0000b80a1553;
  }
  uVar3 = 0x1000000;
  if (bVar4) {
    uVar3 = 0x100000;
  }
  FUN_ffff0000b804d74c(&local_18,6,pcVar2);
  lVar8 = FUN_ffff0000b800c9f4
                    (uVar3,0x10000,0xffffffffffffffff,0,0,
                     s_display_others_boot_logo_compres_ffff0000b8093d1f);
  *param_1 = lVar8;
  if (lVar8 == 0) {
LAB_ffff0000b80517c4:
    FUN_ffff0000b804c33c(s_logo_addr_memory_failed_to_alloc_ffff0000b8090398);
  }
  else {
    plVar1 = param_1 + 2;
    iVar5 = FUN_ffff0000b804a5f8
                      (PTR_DAT_ffff0000b81086d8,s_vm_logo_reserved_ffff0000b8093d43,uVar3,plVar1,0xc
                       ,lVar8,0,0);
    lVar8 = param_1[2];
    if (iVar5 != 0) {
LAB_ffff0000b8051814:
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b744(s_err__d__vm_logo__0x_p__ffff0000b808a2e1,iVar5,lVar8);
    }
    uVar6 = FUN_ffff0000b8050eb8(&DAT_ffff0000b8090393,&local_18,lVar8,uVar3);
    if ((int)uVar6 < 0) {
      FUN_ffff0000b804c33c(s__s_not_loaded__trying_to_load_si_ffff0000b809de71,&local_18);
      FUN_ffff0000b800d264(*param_1);
      FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,param_1[2]);
      lVar8 = FUN_ffff0000b800c9f4
                        (0x1000000,0x10000,0xffffffffffffffff,0,0,
                         s_display_others_boot_logo_compres_ffff0000b8093d1f);
      *param_1 = lVar8;
      if (lVar8 == 0) goto LAB_ffff0000b80517c4;
      iVar5 = FUN_ffff0000b804a5f8
                        (PTR_DAT_ffff0000b81086d8,s_vm_logo_reserved_ffff0000b8093d43,0x1000000,
                         plVar1,0xc,lVar8,0,0);
      lVar8 = *plVar1;
      if (iVar5 != 0) goto LAB_ffff0000b8051814;
      iVar5 = FUN_ffff0000b8050eb8(&DAT_ffff0000b8090393,&DAT_ffff0000b8090393,lVar8,0x1000000);
      if (iVar5 < 0) {
        FUN_ffff0000b804c33c(s_logo_bin_not_loaded_ffff0000b80a1559);
        FUN_ffff0000b800d264(*param_1);
        FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,param_1[2]);
        return iVar5;
      }
    }
    FUN_ffff0000b8082b70(~uVar6 >> 0x1f);
    iVar5 = FUN_ffff0000b803e8ac();
    uVar6 = iVar5 + 0xffffU & 0xffff0000;
    lVar8 = FUN_ffff0000b800c9f4
                      (uVar6,0x10000,0xffffffffffffffff,0,0,
                       s_display_others_boot_logo_decompr_ffff0000b80887c9);
    param_1[1] = lVar8;
    if (lVar8 != 0) {
      iVar5 = FUN_ffff0000b804a5f8
                        (PTR_DAT_ffff0000b81086d8,s_vm_dec_logo_reserved_ffff0000b8086a46,uVar6,
                         param_1 + 3,0xc,lVar8,0,0);
      if (iVar5 == 0) {
        return 0;
      }
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b744(s_err__d__vm_dec_logo__0x_p__ffff0000b809ee3f,iVar5,param_1[3]);
    }
    FUN_ffff0000b804c33c(s_dec_logo_addr_memory_failed_to_a_ffff0000b8085a8b);
    FUN_ffff0000b800d264(*param_1);
    FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,param_1[2]);
  }
  return -5;
}


