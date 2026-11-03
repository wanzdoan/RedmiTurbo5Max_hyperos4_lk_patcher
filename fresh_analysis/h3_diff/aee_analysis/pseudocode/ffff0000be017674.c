
void FUN_ffff0000be017674(undefined8 param_1,byte *param_2,uint param_3)

{
  int iVar1;
  uint uVar2;
  long lVar3;
  char *pcVar4;
  uint uVar5;
  byte *pbVar6;
  ulong uVar7;
  long lVar8;
  int local_48;
  uint local_44;
  
  uVar7 = (ulong)param_3;
  local_48 = 2;
  local_44 = param_3;
  iVar1 = FUN_ffff0000be060cb8(param_1,s_crclist_ffff0000be0bbd30);
  if (iVar1 == 0) {
    param_2[uVar7] = 0;
    iVar1 = FUN_ffff0000be0171a8(param_2);
    if (iVar1 != 0) {
      pcVar4 = s_update_crc_list_failed_ffff0000be0c4f49;
      goto LAB_ffff0000be017814;
    }
    pcVar4 = s_loaded_crclist_ffff0000be0b620a;
  }
  else {
    iVar1 = FUN_ffff0000be060cb8(param_1,s_sparsecrclist_ffff0000be0c0598);
    if (iVar1 != 0) {
      iVar1 = FUN_ffff0000be060fa0(param_1,&DAT_ffff0000be0b5016,4);
      if (iVar1 == 0) {
        iVar1 = FUN_ffff0000be027ee8(param_2,uVar7);
        if (iVar1 != 0) {
          pcVar4 = s_write_gpt_failed__ffff0000be0bac18;
          goto LAB_ffff0000be017814;
        }
        goto LAB_ffff0000be0177ac;
      }
      lVar3 = FUN_ffff0000be05df6c(param_1);
      if ((lVar3 == 0) && (lVar3 = FUN_ffff0000be05de5c(param_1), lVar3 == 0)) {
        pcVar4 = s_partition_does_not_exist_ffff0000be0c3d06;
LAB_ffff0000be017814:
        FUN_ffff0000be0137e8(pcVar4);
        return;
      }
      if (DAT_ffff0000be10872c == '\x01') {
        lVar8 = 0;
LAB_ffff0000be017720:
        iVar1 = FUN_ffff0000be060cb8(param_1,&DAT_ffff0000be10842c + lVar8);
        if (iVar1 != 0) goto code_r0xffff0000be017730;
        if (param_3 == 0) {
          uVar2 = 0;
        }
        else {
          uVar2 = 0xffffffff;
          pbVar6 = param_2;
          uVar5 = param_3;
          do {
            uVar5 = uVar5 - 1;
            uVar2 = *(uint *)(&DAT_ffff0000be0c777c + ((ulong)(*pbVar6 ^ uVar2) & 0xff) * 4) ^
                    uVar2 >> 8;
            pbVar6 = pbVar6 + 1;
          } while (uVar5 != 0);
          uVar2 = ~uVar2;
        }
        if (uVar2 != *(uint *)((long)&DAT_ffff0000be108440 + lVar8)) {
          FUN_ffff0000be0137e8(s_check_crc_failed_ffff0000be0a6a95);
          FUN_ffff0000be05def0(lVar3);
          return;
        }
LAB_ffff0000be017844:
        FUN_ffff0000be05fd98(s_CRC_verification_pass_ffff0000be0bceef);
      }
      iVar1 = FUN_ffff0000be060fa0(param_1,s_preloader_ffff0000be0a8e9d,9);
      if (iVar1 == 0) {
        iVar1 = FUN_ffff0000be015d1c(param_2,&local_44);
        if (iVar1 != 0) {
          FUN_ffff0000be0137e8(s_process_preloader_fail__ffff0000be0bbd38);
          return;
        }
        iVar1 = thunk_FUN_ffff0000be0852d8();
        if (iVar1 == 0) {
LAB_ffff0000be0179a4:
          iVar1 = FUN_ffff0000be067c48(param_1,param_2,local_44);
          param_3 = local_44;
          if (iVar1 == 0) goto LAB_ffff0000be017868;
          pcVar4 = s_Image_platform_not_match_with_de_ffff0000be0a4416;
        }
        else {
          iVar1 = FUN_ffff0000be05e154(lVar3,0x102,&local_48);
          if (iVar1 == 0) {
            if (local_48 == 2) {
              lVar8 = 0x1000;
            }
            else {
              if (local_48 != 1) {
                pcVar4 = s_unsupport_flash_type_ffff0000be0af604;
                goto LAB_ffff0000be0179d4;
              }
              lVar8 = 0x800;
            }
            iVar1 = FUN_ffff0000be067a9c(param_2 + lVar8,local_44 - (int)lVar8);
            if (iVar1 == 0) goto LAB_ffff0000be0179a4;
            pcVar4 = s_Preloader_auth_failed__ffff0000be0ac615;
          }
          else {
            FUN_ffff0000be05fd98(s_bio_get_bootdev_type_fialed_ffff0000be0c05a6);
            pcVar4 = s_bio_get_bootdev_type_fialed_ffff0000be0b8679;
          }
        }
      }
      else {
LAB_ffff0000be017868:
        if (*(long *)(lVar3 + 0x20) < (long)(ulong)(param_3 + 0x1ff & 0xfffffe00)) {
          pcVar4 = s_data_size_is_larger_than_partiti_ffff0000be0c4f60;
        }
        else {
          uVar2 = FUN_ffff0000be05e088(lVar3,param_2,0,param_3);
          if (local_44 == uVar2) {
            if ((*(char *)(lVar3 + 0x50) == '\0') &&
               (iVar1 = FUN_ffff0000be0624a4(*(undefined8 *)(lVar3 + 0x18),0), 0 < iVar1)) {
              FUN_ffff0000be05ea50();
            }
            FUN_ffff0000be013828(&DAT_ffff0000be0ae340);
            goto LAB_ffff0000be0179d8;
          }
          pcVar4 = s_flash_write_failure_ffff0000be0a443c;
        }
      }
LAB_ffff0000be0179d4:
      FUN_ffff0000be0137e8(pcVar4);
LAB_ffff0000be0179d8:
      FUN_ffff0000be05def0(lVar3);
      return;
    }
    param_2[uVar7] = 0;
    iVar1 = FUN_ffff0000be016d7c(param_2);
    if (iVar1 != 0) {
      pcVar4 = s_update_sparse_crc_list_failed_ffff0000be0c191d;
      goto LAB_ffff0000be017814;
    }
    pcVar4 = s_loaded_sparsecrclist_ffff0000be0b8663;
  }
  FUN_ffff0000be05fd98(pcVar4);
LAB_ffff0000be0177ac:
  FUN_ffff0000be013828(&DAT_ffff0000be0ae340);
  return;
code_r0xffff0000be017730:
  lVar8 = lVar8 + 0x18;
  if (lVar8 == 0x300) goto LAB_ffff0000be017844;
  goto LAB_ffff0000be017720;
}


