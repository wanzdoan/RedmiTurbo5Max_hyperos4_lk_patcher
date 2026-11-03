
undefined8 FUN_ffff0000be0093f8(void)

{
  long lVar1;
  bool bVar2;
  undefined4 uVar3;
  int iVar4;
  int iVar5;
  long *plVar6;
  ulong uVar7;
  long *plVar8;
  long lVar9;
  long lVar10;
  ulong local_f0;
  ulong uStack_e8;
  undefined8 local_e0;
  undefined8 local_d8;
  undefined8 local_d0;
  ulong local_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 local_a8;
  undefined8 local_a0;
  ulong local_98;
  undefined8 local_90;
  undefined8 local_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  long local_58;
  undefined8 local_38;
  
  plVar6 = (long *)FUN_ffff0000be011a20(s_mrdump_ffff0000be0bdf63);
  if (plVar6 == (long *)0x0) {
    return 0xfffffffe;
  }
  if (*(ulong *)(plVar6[2] + 0x20) >> 0x1c == 0) {
    FUN_ffff0000be0115fc
              (s_mrdump_partition__llx__is_not_en_ffff0000be0a4210,*(ulong *)(plVar6[2] + 0x20),
               0x10000000);
    FUN_ffff0000be05fd98
              (s__s__d__mini_dump_expdb_fail_ffff0000be0ac3bc,s_dump_expdb_to_mpart_ffff0000be0bf2d2
               ,0x17d);
LAB_ffff0000be0094d4:
    thunk_FUN_ffff0000be09937c(plVar6);
    return 0xffffffff;
  }
  local_60 = 0;
  local_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_88 = 0;
  FUN_ffff0000be0114f0(s_Output_to_MRDUMP_Partition__s_ffff0000be0bbaa1,*plVar6);
  FUN_ffff0000be060734(&local_88,0x38);
  uVar7 = (*(code *)plVar6[3])(plVar6,0,&local_88,0x38);
  if ((uVar7 & 1) == 0) {
    FUN_ffff0000be0115fc(s_SDCard__Reading_InfoHeader_faile_ffff0000be0ad221);
    goto LAB_ffff0000be0094ec;
  }
  if (local_58 != 0) {
    FUN_ffff0000be0115fc(s_EXPDB_DUMP_SIZE_is_not_Zero__Abo_ffff0000be0ac3d9);
    thunk_FUN_ffff0000be09937c(plVar6);
    return 0xfffffffd;
  }
  plVar8 = (long *)FUN_ffff0000be05f204(0x10,0x10020);
  if (plVar8 == (long *)0x0) {
    FUN_ffff0000be05fd98
              (s__s__d__No_enough_memory__ffff0000be0c4ce3,s_dump_expdb_to_mpart_ffff0000be0bf2d2,
               0x19c);
    FUN_ffff0000be0115fc(s_No_enough_memory_or_alloc_memory_ffff0000be0b07e3);
    thunk_FUN_ffff0000be09937c(plVar6);
    return 0xfffffffc;
  }
  FUN_ffff0000be060ae0(plVar8,0,0x10020);
  *plVar8 = (long)plVar6;
  local_60 = CONCAT44(local_60._4_4_,1);
  local_58 = -1;
  uVar3 = FUN_ffff0000be0553a4(0,0,0);
  uVar3 = FUN_ffff0000be0553a4(uVar3,&local_88,0x24);
  uVar3 = FUN_ffff0000be0553a4(uVar3,&local_60,0x10);
  uStack_68 = CONCAT44(uVar3,(undefined4)uStack_68);
  uVar7 = (**(code **)(*plVar8 + 0x20))(*plVar8,0,&local_88,0x38);
  if ((uVar7 & 1) == 0) {
LAB_ffff0000be009900:
    FUN_ffff0000be0115fc(s_SDCard__Write_InfoHeader_error__ffff0000be0b2a8d);
  }
  else {
    FUN_ffff0000be05fd98
              (s__s__d__write_ULLONG_MAX_to_expdb_ffff0000be0c3b3a,
               s_dump_expdb_to_mpart_ffff0000be0bf2d2,0x1b7);
    plVar8[2] = 0x1000;
    FUN_ffff0000be0357ec();
    lVar9 = FUN_ffff0000be00ade4(plVar8,FUN_ffff0000be0099a8);
    if (lVar9 != 0) {
      uVar7 = FUN_ffff0000be0658b0();
      if ((uVar7 & 1) != 0) {
        lVar1 = lVar9 + 0x1c20;
        FUN_ffff0000be009030(lVar1,0x20);
        FUN_ffff0000be0090f0(lVar9 + 0x1e50,0x10);
        FUN_ffff0000be009188(lVar9 + 0x1c48,lVar1,0x20);
        local_f0 = CONCAT44(local_f0._4_4_,0x100);
        lVar10 = FUN_ffff0000be05f1bc(0x100);
        if (lVar10 == 0) {
          FUN_ffff0000be05fd98
                    (s__s__d__alloc_emsg_fail__no_enoug_ffff0000be0b602a,
                     s_dump_expdb_to_mpart_ffff0000be0bf2d2,0x1ca);
                    /* WARNING: Subroutine does not return */
          FUN_ffff0000be05ee98
                    (s_app_aee_mrdump_mpart_c_ffff0000be0a8c60,0x1cb,&DAT_ffff0000be0a553d);
        }
        FUN_ffff0000be060ae0(lVar10,0,0x100);
        iVar4 = FUN_ffff0000be06742c
                          (lVar1,0x20,&DAT_ffff0000be0d5ce8,3,&DAT_ffff0000be0d5cf1,0x100,lVar10,
                           &local_f0);
        if ((iVar4 != 0) || ((int)local_f0 != 0x100)) {
          FUN_ffff0000be05fd98
                    (s__s__d__RSA_encrypt_fail__ret__d__ffff0000be0af392,
                     s_dump_expdb_to_mpart_ffff0000be0bf2d2,0x1d7,iVar4);
                    /* WARNING: Subroutine does not return */
          FUN_ffff0000be05ee98
                    (s_app_aee_mrdump_mpart_c_ffff0000be0a8c60,0x1d8,&DAT_ffff0000be0a553d);
        }
        FUN_ffff0000be0607b0(lVar9 + 0x1e60,lVar10,0x100);
        FUN_ffff0000be060ae0(lVar10,0,local_f0 & 0xffffffff);
        iVar4 = FUN_ffff0000be06742c
                          (lVar9 + 0x1e50,0x10,&DAT_ffff0000be0d5ce8,3,&DAT_ffff0000be0d5cf1,0x100,
                           lVar10,&local_f0);
        if ((iVar4 != 0) || ((int)local_f0 != 0x100)) {
          FUN_ffff0000be05fd98
                    (s__s__d__RSA_encrypt_fail__ret__d__ffff0000be0af392,
                     s_dump_expdb_to_mpart_ffff0000be0bf2d2,0x1e2,iVar4);
                    /* WARNING: Subroutine does not return */
          FUN_ffff0000be05ee98
                    (s_app_aee_mrdump_mpart_c_ffff0000be0a8c60,0x1e3,&DAT_ffff0000be0a553d);
        }
        FUN_ffff0000be0607b0(lVar9 + 0x1f60,lVar10,0x100);
        thunk_FUN_ffff0000be09937c(lVar10);
        *(undefined4 *)(lVar9 + 0x1c40) = 1;
      }
      iVar4 = 0;
      bVar2 = true;
      local_38 = 0;
      local_90 = 0;
      local_a0 = 0;
      local_98 = 0;
      local_b0 = 0;
      local_a8 = 0;
      local_c0 = 0;
      uStack_b8 = 0;
      local_d0 = 0;
      local_c8 = 0;
      local_e0 = 0;
      local_d8 = 0;
      local_f0 = 0;
      uStack_e8 = 0;
      do {
        iVar5 = FUN_ffff0000be00cf98(iVar4,&local_38,(long)&local_90 + 4,&local_90);
        if (iVar5 == 0) {
          uStack_e8 = local_90 & 0xffffffff;
          local_f0 = local_90 >> 0x20;
          local_e0 = CONCAT44(local_e0._4_4_,2);
          local_d8 = 0;
          local_d0 = 0;
          local_c8 = local_c8 & 0xffffffff00000000;
          local_c0 = 0;
          uStack_b8 = 0;
          local_b0 = CONCAT44(local_b0._4_4_,2);
          local_a8 = 0;
          local_a0 = 0;
          local_98 = local_98 & 0xffffffff00000000;
          uVar7 = FUN_ffff0000be00bac4(lVar9,&local_f0,local_38);
          if ((uVar7 & 1) == 0) {
            bVar2 = false;
          }
        }
        iVar4 = iVar4 + 1;
      } while (iVar4 != 0x80);
      if (!bVar2) {
        FUN_ffff0000be05fd98
                  (s__s__d__kzip_add_file_expdb_fail_ffff0000be0b3b66,
                   s_dump_expdb_to_mpart_ffff0000be0bf2d2,0x206);
        FUN_ffff0000be00ae40(lVar9);
        plVar6 = plVar8;
        goto LAB_ffff0000be0094d4;
      }
      FUN_ffff0000be0357ec();
      FUN_ffff0000be00ae40(lVar9);
      FUN_ffff0000be0099a8(plVar8,0,0);
      FUN_ffff0000be0114f0(s_Zip_EXPDB_size_is___lld_ffff0000be0a79a3,plVar8[1]);
      DAT_ffff0000be0f90c8 = plVar8[1];
      local_58 = DAT_ffff0000be0f90c8;
      uVar3 = FUN_ffff0000be0553a4(0,0,0);
      uVar3 = FUN_ffff0000be0553a4(uVar3,&local_88,0x24);
      uVar3 = FUN_ffff0000be0553a4(uVar3,&local_60,0x10);
      uStack_68 = CONCAT44(uVar3,(undefined4)uStack_68);
      uVar7 = (**(code **)(*plVar8 + 0x20))(*plVar8,0,&local_88,0x38);
      if ((uVar7 & 1) != 0) {
        FUN_ffff0000be05fd98
                  (s__s__d__write_real_expdb_size_to_e_ffff0000be0a9ee7,
                   s_dump_expdb_to_mpart_ffff0000be0bf2d2,0x22b);
        thunk_FUN_ffff0000be09937c(plVar8);
        thunk_FUN_ffff0000be09937c(plVar6);
        FUN_ffff0000be0357ec();
        FUN_ffff0000be0114f0(s_OUTPUT__MODE__PARTITION_DATA_ffff0000be0a556a);
        return 0;
      }
      goto LAB_ffff0000be009900;
    }
    FUN_ffff0000be05fd98
              (s__s__d__kzip_open_handle_fail_ffff0000be0c1782,
               s_dump_expdb_to_mpart_ffff0000be0bf2d2,0x210);
  }
  thunk_FUN_ffff0000be09937c(plVar8);
LAB_ffff0000be0094ec:
  thunk_FUN_ffff0000be09937c(plVar6);
  return 0xfffffffe;
}


