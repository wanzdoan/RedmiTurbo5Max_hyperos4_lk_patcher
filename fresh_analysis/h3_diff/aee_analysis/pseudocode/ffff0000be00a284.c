
int FUN_ffff0000be00a284(long param_1,undefined8 param_2,undefined8 param_3)

{
  undefined *puVar1;
  int iVar2;
  int iVar3;
  long lVar4;
  long lVar5;
  ulong uVar6;
  long lVar7;
  long lVar8;
  char *pcVar9;
  undefined8 uVar10;
  int iVar11;
  int local_c4;
  undefined8 local_c0;
  undefined8 local_b8;
  ulong local_b0;
  ulong uStack_a8;
  undefined8 local_a0;
  undefined8 local_98;
  undefined8 local_90;
  ulong local_88;
  undefined8 local_80;
  undefined8 uStack_78;
  ulong local_70;
  undefined8 local_68;
  undefined8 local_60;
  ulong local_58;
  
  local_60 = 0;
  local_58 = 0;
  local_70 = 0;
  local_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  local_88 = 0;
  local_a0 = 0;
  local_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  local_b8 = 0;
  FUN_ffff0000be0114f0(s_Output_by_USB_ffff0000be0baa2d);
  lVar4 = FUN_ffff0000be05f204(0x40,0xfb48);
  if (lVar4 == 0) {
    FUN_ffff0000be05fd98
              (s__s__d__No_enough_memory__ffff0000be0b72f7,s_mrdump_usb_output_ffff0000be0a79bd,
               0x104);
    return -1;
  }
  FUN_ffff0000be060ae0(lVar4,0,0xfb48);
  FUN_ffff0000be05ede4(100000);
  FUN_ffff0000be0357ec();
  lVar5 = FUN_ffff0000be00ade4(lVar4,FUN_ffff0000be00a6b0);
  iVar2 = 0;
  if (lVar5 == 0) {
LAB_ffff0000be00a5ac:
    thunk_FUN_ffff0000be09937c(lVar4);
    FUN_ffff0000be0357ec();
    uVar10 = FUN_ffff0000be009d40(*(undefined4 *)(param_1 + 0xf8));
    FUN_ffff0000be0114f0(s_OUTPUT__s_MODE__s_ffff0000be0b6054,&DAT_ffff0000be0a8c9b,uVar10);
    puVar1 = &DAT_ffff0000be0a55e3;
    if (iVar2 == 0) {
      puVar1 = &DAT_ffff0000be0ac40e;
    }
    FUN_ffff0000be0114f0(s_mrdump_output_completed__s__ffff0000be0b7310,puVar1);
    return iVar2 + -1;
  }
  uVar6 = FUN_ffff0000be0658b0();
  if ((uVar6 & 1) != 0) {
    lVar8 = lVar5 + 0x1c20;
    FUN_ffff0000be009030(lVar8,0x20);
    FUN_ffff0000be0090f0(lVar5 + 0x1e50,0x10);
    FUN_ffff0000be009188(lVar5 + 0x1c48,lVar8,0x20);
    local_c4 = 0x100;
    lVar7 = FUN_ffff0000be05f1bc(0x100);
    if (lVar7 == 0) {
      FUN_ffff0000be05fd98
                (s__s__d__alloc_emsg_fail__no_enoug_ffff0000be0b602a,
                 s_mrdump_usb_output_ffff0000be0a79bd,0x117);
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee98(s_app_aee_mrdump_usb_c_ffff0000be0b2aaf,0x118,&DAT_ffff0000be0a553d);
    }
    FUN_ffff0000be060ae0(lVar7,0,0x100);
    iVar2 = FUN_ffff0000be06742c
                      (lVar8,0x20,&DAT_ffff0000be0d5ceb,3,&DAT_ffff0000be0d5df1,0x100,lVar7,
                       &local_c4);
    if ((iVar2 != 0) || (local_c4 != 0x100)) {
      FUN_ffff0000be05fd98
                (s__s__d__RSA_encrypt_fail__ret__d__ffff0000be0af392,
                 s_mrdump_usb_output_ffff0000be0a79bd,0x124,iVar2);
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee98(s_app_aee_mrdump_usb_c_ffff0000be0b2aaf,0x125,&DAT_ffff0000be0a553d);
    }
    FUN_ffff0000be0607b0(lVar5 + 0x1e60,lVar7,0x100);
    FUN_ffff0000be060ae0(lVar7,0,local_c4);
    iVar2 = FUN_ffff0000be06742c
                      (lVar5 + 0x1e50,0x10,&DAT_ffff0000be0d5ceb,3,&DAT_ffff0000be0d5df1,0x100,lVar7
                       ,&local_c4);
    if ((iVar2 != 0) || (local_c4 != 0x100)) {
      FUN_ffff0000be05fd98
                (s__s__d__RSA_encrypt_fail__ret__d__ffff0000be0af392,
                 s_mrdump_usb_output_ffff0000be0a79bd,0x12f,iVar2);
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee98(s_app_aee_mrdump_usb_c_ffff0000be0b2aaf,0x130,&DAT_ffff0000be0a553d);
    }
    FUN_ffff0000be0607b0(lVar5 + 0x1f60,lVar7,0x100);
    thunk_FUN_ffff0000be09937c(lVar7);
    *(undefined4 *)(lVar5 + 0x1c40) = 1;
  }
  lVar8 = FUN_ffff0000be060d00(s_SYS_COREDUMP_ffff0000be0af3c1);
  if (lVar8 == 0) {
    pcVar9 = s__s__d__No_enough_memory_for_SYS__ffff0000be0a553f;
    uVar10 = 0x13b;
  }
  else {
    uVar6 = FUN_ffff0000be00bac4(lVar5,param_2,lVar8);
    if ((uVar6 & 1) != 0) {
      lVar8 = FUN_ffff0000be060d00(s_kernel_cmm_ffff0000be0bf2c7);
      if (lVar8 == 0) {
        pcVar9 = s__s__d__No_enough_memory_for_kern_ffff0000be0a41e7;
        uVar10 = 0x147;
        goto LAB_ffff0000be00a574;
      }
      uVar6 = FUN_ffff0000be00bac4(lVar5,param_3,lVar8);
      if ((uVar6 & 1) != 0) {
        iVar11 = 0;
        iVar2 = 1;
        do {
          iVar3 = FUN_ffff0000be00cf98(iVar11,&local_b8,(long)&local_c0 + 4,&local_c0);
          if (iVar3 == 0) {
            uStack_a8 = local_c0 & 0xffffffff;
            local_b0 = local_c0 >> 0x20;
            local_a0 = CONCAT44(local_a0._4_4_,2);
            local_98 = 0;
            local_90 = 0;
            local_88 = local_88 & 0xffffffff00000000;
            local_80 = 0;
            uStack_78 = 0;
            local_70 = local_70 & 0xffffffff00000000;
            local_68 = 0;
            local_60 = 0;
            local_58 = local_58 & 0xffffffff00000000;
            uVar6 = FUN_ffff0000be00bac4(lVar5,&local_b0,local_b8);
            if ((uVar6 & 1) == 0) {
              iVar2 = 0;
            }
          }
          iVar11 = iVar11 + 1;
        } while (iVar11 != 0x80);
        FUN_ffff0000be00ae40(lVar5);
        FUN_ffff0000be00a6b0(lVar4,0,0);
        goto LAB_ffff0000be00a5ac;
      }
    }
    pcVar9 = s__s__d__failed_to_generate_cmm_ffff0000be0baa3c;
    uVar10 = 0x152;
  }
LAB_ffff0000be00a574:
  FUN_ffff0000be05fd98(pcVar9,s_mrdump_usb_output_ffff0000be0a79bd,uVar10);
  FUN_ffff0000be00ae40(lVar5);
  thunk_FUN_ffff0000be09937c(lVar4);
  return -1;
}


