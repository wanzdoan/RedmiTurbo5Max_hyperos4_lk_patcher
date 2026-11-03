
int FUN_ffff0000be011e94(long param_1,undefined8 param_2,undefined8 param_3,undefined8 *param_4)

{
  uint uVar1;
  int iVar2;
  undefined4 uVar3;
  ulong uVar4;
  long *plVar5;
  long lVar6;
  long lVar7;
  long lVar8;
  undefined8 uVar9;
  char *pcVar10;
  long lVar11;
  ulong uVar12;
  ulong uVar13;
  undefined8 local_88;
  uint local_80;
  undefined4 uStack_7c;
  undefined4 uStack_78;
  undefined4 uStack_74;
  undefined4 local_70;
  undefined4 uStack_6c;
  undefined4 uStack_68;
  int local_64;
  int local_44;
  
  if (param_4 == (undefined8 *)0x0) {
    return -1;
  }
  uVar4 = FUN_ffff0000be0658b0();
  if (((uVar4 & 1) != 0) && (iVar2 = FUN_ffff0000be0093f8(), iVar2 < 0)) {
    FUN_ffff0000be05fd98
              (s__s__d__dump_expdb_fail_ffff0000be0ad209,s_mrdump_ext4_output_ffff0000be0ab2de,0x13d
              );
    return iVar2;
  }
  local_70 = 0;
  uStack_6c = 0;
  uStack_68 = 0;
  local_64 = 0;
  local_80 = 0;
  uStack_7c = 0;
  uStack_78 = 0;
  uStack_74 = 0;
  local_88 = 0;
  FUN_ffff0000be0114f0(s_Output_to_EXT4_Partition__s_ffff0000be0ae468,*param_4);
  FUN_ffff0000be060734(&local_88,0x28);
  FUN_ffff0000be012594(&local_88,s_first_check_ffff0000be0b4f7a);
  if (*(uint *)(param_1 + 0xf0) == 0) {
    FUN_ffff0000be05fd98
              (s__s__d__Pre_Allocate_has_no_LBA_m_ffff0000be0b2bce,
               s_mrdump_ext4_output_ffff0000be0ab2de,0x14b,0);
    return -3;
  }
  uVar4 = (*(code *)param_4[3])(param_4,(ulong)*(uint *)(param_1 + 0xf0) << 0xc,&local_88,0x28);
  iVar2 = local_64;
  uVar1 = local_80;
  if ((uVar4 & 1) == 0) {
    FUN_ffff0000be05fd98
              (s__s__d__SDCard__Reading_InfoLBA_f_ffff0000be0b4f88,
               s_mrdump_ext4_output_ffff0000be0ab2de,0x150);
    return -1;
  }
  uVar4 = local_88 & 0xffff;
  if ((short)local_88 == 2) {
    uVar12 = (ulong)local_88._4_4_;
    if (*(uint *)(param_1 + 0xf0) != local_88._4_4_) {
      FUN_ffff0000be012594
                (&local_88,s_LBA_Starting_Address_Error_LBA0__ffff0000be0b743f,local_88._4_4_);
      return -3;
    }
    lVar11 = CONCAT44(local_70,uStack_74);
    if (lVar11 == 0) {
      lVar11 = CONCAT44(uStack_78,uStack_7c);
      if (lVar11 != 0) {
        DAT_ffff0000be106930 = CONCAT44(uStack_68,uStack_6c);
        uVar13 = (ulong)local_80;
        uVar3 = FUN_ffff0000be0553a4(0,0,0);
        uVar4 = FUN_ffff0000be0553a4(uVar3,&local_88,0x24);
        if (iVar2 == (int)uVar4) {
          plVar5 = (long *)FUN_ffff0000be05f204(0x10,0x11030);
          if (plVar5 == (long *)0x0) {
            FUN_ffff0000be05fd98
                      (s__s__d__No_enough_memory__ffff0000be0c4ce3,
                       s_mrdump_ext4_output_ffff0000be0ab2de,0x184);
            FUN_ffff0000be0115fc(s_No_enough_memory_or_alloc_memory_ffff0000be0b07e3);
            return -4;
          }
          FUN_ffff0000be060ae0(plVar5,0,0x11030);
          uVar3 = *(undefined4 *)(param_1 + 0xf0);
          *plVar5 = (long)param_4;
          plVar5[1] = lVar11;
          *(undefined4 *)((long)plVar5 + 0x1c) = uVar3;
          *(byte *)((long)plVar5 + 0x1102c) = local_88._2_1_ & 1;
          if ((local_88 & 0x10000) != 0) {
            FUN_ffff0000be0113f4(s_Do_full_check_ffff0000be0c4d72);
          }
          uStack_74 = 0xffffffff;
          local_70 = 0xffffffff;
          uVar3 = FUN_ffff0000be0553a4(0,0,0);
          local_64 = FUN_ffff0000be0553a4(uVar3,&local_88,0x24);
          lVar11 = uVar12 << 0xc;
          uVar4 = (**(code **)(*plVar5 + 0x20))(*plVar5,lVar11,&local_88,0x28);
          if ((uVar4 & 1) == 0) {
            uVar9 = 0x19c;
LAB_ffff0000be012440:
            FUN_ffff0000be05fd98
                      (s__s__d__SDCard__Write_InfoLBA_err_ffff0000be0b9947,
                       s_mrdump_ext4_output_ffff0000be0ab2de,uVar9);
LAB_ffff0000be012458:
            thunk_FUN_ffff0000be09937c(plVar5);
            return -1;
          }
          *(uint *)((long)plVar5 + 0x1c) = uVar1;
          uVar4 = (**(code **)(*plVar5 + 0x18))(*plVar5,uVar13 << 0xc,(long)plVar5 + 0x2c,0x1000);
          if ((uVar4 & 1) == 0) {
            FUN_ffff0000be012594(&local_88,s_SDCard__Reading_BlockLBA_error__ffff0000be0c046e);
            goto LAB_ffff0000be012458;
          }
          *(undefined4 *)(plVar5 + 3) =
               *(undefined4 *)((long)plVar5 + (ulong)*(uint *)((long)plVar5 + 0x24) * 4 + 0x2c);
          iVar2 = FUN_ffff0000be011ce8(plVar5,(int)plVar5[4]);
          *(int *)(plVar5 + 5) = iVar2;
          if (iVar2 == 0) {
            FUN_ffff0000be05fd98
                      (s__s__d__No_continuous_space__ffff0000be0aa04a,
                       s_mrdump_ext4_output_ffff0000be0ab2de,0x1ac);
            goto LAB_ffff0000be0124dc;
          }
          FUN_ffff0000be0114f0(s_Pre_Allocate_starts_at_LBA___u_ffff0000be0aa067,uVar12);
          FUN_ffff0000be0114f0(s_SYS_COREDUMP_starts_at_LBA___u_ffff0000be0aa088,(int)plVar5[3]);
          FUN_ffff0000be0357ec();
          lVar6 = FUN_ffff0000be00ade4(plVar5,FUN_ffff0000be009b24);
          if (lVar6 == 0) goto LAB_ffff0000be0124dc;
          uVar4 = FUN_ffff0000be0658b0();
          if ((uVar4 & 1) != 0) {
            lVar8 = lVar6 + 0x1c20;
            FUN_ffff0000be009030(lVar8,0x20);
            FUN_ffff0000be0090f0(lVar6 + 0x1e50,0x10);
            FUN_ffff0000be009188(lVar6 + 0x1c48,lVar8,0x20);
            local_44 = 0x100;
            lVar7 = FUN_ffff0000be05f1bc(0x100);
            if (lVar7 == 0) {
              FUN_ffff0000be05fd98
                        (s__s__d__alloc_emsg_fail__no_enoug_ffff0000be0b602a,
                         s_mrdump_ext4_output_ffff0000be0ab2de,0x1c3);
                    /* WARNING: Subroutine does not return */
              FUN_ffff0000be05ee98
                        (s_app_aee_mrdump_datafs_c_ffff0000be0ae485,0x1c4,&DAT_ffff0000be0a553d);
            }
            FUN_ffff0000be060ae0(lVar7,0,0x100);
            iVar2 = FUN_ffff0000be06742c
                              (lVar8,0x20,&DAT_ffff0000be0d5cee,3,&DAT_ffff0000be0d5ef1,0x100,lVar7,
                               &local_44);
            if ((iVar2 != 0) || (local_44 != 0x100)) {
              FUN_ffff0000be05fd98
                        (s__s__d__RSA_encrypt_fail__ret__d__ffff0000be0af392,
                         s_mrdump_ext4_output_ffff0000be0ab2de,0x1ce,iVar2);
                    /* WARNING: Subroutine does not return */
              FUN_ffff0000be05ee98
                        (s_app_aee_mrdump_datafs_c_ffff0000be0ae485,0x1cf,&DAT_ffff0000be0a553d);
            }
            FUN_ffff0000be0607b0(lVar6 + 0x1e60,lVar7,0x100);
            FUN_ffff0000be060ae0(lVar7,0,local_44);
            iVar2 = FUN_ffff0000be06742c
                              (lVar6 + 0x1e50,0x10,&DAT_ffff0000be0d5cee,3,&DAT_ffff0000be0d5ef1,
                               0x100,lVar7,&local_44);
            if ((iVar2 != 0) || (local_44 != 0x100)) {
              FUN_ffff0000be05fd98
                        (s__s__d__RSA_encrypt_fail__ret__d__ffff0000be0af392,
                         s_mrdump_ext4_output_ffff0000be0ab2de,0x1d9,iVar2);
                    /* WARNING: Subroutine does not return */
              FUN_ffff0000be05ee98
                        (s_app_aee_mrdump_datafs_c_ffff0000be0ae485,0x1da,&DAT_ffff0000be0a553d);
            }
            FUN_ffff0000be0607b0(lVar6 + 0x1f60,lVar7,0x100);
            thunk_FUN_ffff0000be09937c(lVar7);
            *(undefined4 *)(lVar6 + 0x1c40) = 1;
          }
          lVar8 = FUN_ffff0000be060d00(s_SYS_COREDUMP_ffff0000be0af3c1);
          if (lVar8 == 0) {
            pcVar10 = s__s__d__No_enough_memory_for_SYS__ffff0000be0a553f;
            uVar9 = 0x205;
          }
          else {
            uVar4 = FUN_ffff0000be00bac4(lVar6,param_2,lVar8);
            if ((uVar4 & 1) == 0) {
              FUN_ffff0000be0357ec();
              FUN_ffff0000be00ae40(lVar6);
              FUN_ffff0000be009b24(plVar5,0,0);
              goto LAB_ffff0000be0124dc;
            }
            lVar8 = FUN_ffff0000be060d00(s_kernel_cmm_ffff0000be0bf2c7);
            if (lVar8 != 0) {
              uVar4 = FUN_ffff0000be00bac4(lVar6,param_3,lVar8);
              FUN_ffff0000be0357ec();
              FUN_ffff0000be00ae40(lVar6);
              FUN_ffff0000be009b24(plVar5,0,0);
              if ((uVar4 & 1) == 0) goto LAB_ffff0000be0124dc;
              FUN_ffff0000be0114f0(s_SYS_COREDUMP_ends_at_LBA___u_ffff0000be0ae49d,(int)plVar5[3]);
              FUN_ffff0000be0114f0(s_Zip_COREDUMP_size_is___lld_ffff0000be0c17a0,plVar5[2]);
              uStack_74 = (undefined4)plVar5[2];
              local_70 = (undefined4)((ulong)plVar5[2] >> 0x20);
              uVar3 = FUN_ffff0000be0553a4(0,0,0);
              local_64 = FUN_ffff0000be0553a4(uVar3,&local_88,0x24);
              uVar4 = (**(code **)(*plVar5 + 0x20))(*plVar5,lVar11,&local_88,0x28);
              if ((uVar4 & 1) != 0) {
                thunk_FUN_ffff0000be09937c(plVar5);
                FUN_ffff0000be0357ec();
                uVar9 = FUN_ffff0000be009d40(*(undefined4 *)(param_1 + 0xf8));
                FUN_ffff0000be0114f0
                          (s_OUTPUT__s_MODE__s_ffff0000be0b6054,s_DATA_FS_ffff0000be0a42ea,uVar9);
                return 0;
              }
              uVar9 = 0x233;
              goto LAB_ffff0000be012440;
            }
            pcVar10 = s__s__d__No_enough_memory_for_kern_ffff0000be0a41e7;
            uVar9 = 0x210;
          }
          FUN_ffff0000be05fd98(pcVar10,s_mrdump_ext4_output_ffff0000be0ab2de,uVar9);
          FUN_ffff0000be00ae40(lVar6);
LAB_ffff0000be0124dc:
          thunk_FUN_ffff0000be09937c(plVar5);
          return -4;
        }
        pcVar10 = s_InfoLBA_CRC32_Error__Abort___CRC_ffff0000be0ad30a;
        goto LAB_ffff0000be011fe8;
      }
      pcVar10 = s_Allocate_file_with_zero_size__Ab_ffff0000be0b9911;
      lVar11 = 0;
    }
    else {
      pcVar10 = s_CORE_DUMP_SIZE_is_not_Zero__Abor_ffff0000be0ab19a;
    }
    FUN_ffff0000be012594(&local_88,pcVar10,lVar11);
  }
  else {
    pcVar10 = s_Unsupport_PAF_version__d__expect_ffff0000be0c3c33;
    iVar2 = 2;
LAB_ffff0000be011fe8:
    FUN_ffff0000be012594(&local_88,pcVar10,uVar4,iVar2);
  }
  return -3;
}


