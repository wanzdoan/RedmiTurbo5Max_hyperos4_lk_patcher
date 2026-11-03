// ffff00009070e424  size=904  FUN_ffff00009070e424

void FUN_ffff00009070e424(undefined8 param_1,byte *param_2,uint param_3)

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
  iVar1 = FUN_ffff00009077f0ac(param_1,s_crclist_ffff0000907f26dc);
  if (iVar1 == 0) {
    param_2[uVar7] = 0;
    iVar1 = FUN_ffff00009070df58(param_2);
    if (iVar1 != 0) {
      pcVar4 = s_update_crc_list_failed_ffff0000908003ff;
      goto LAB_ffff00009070e5c4;
    }
    pcVar4 = s_loaded_crclist_ffff0000907ea6c6;
  }
  else {
    iVar1 = FUN_ffff00009077f0ac(param_1,s_sparsecrclist_ffff0000907f938f);
    if (iVar1 != 0) {
      iVar1 = FUN_ffff00009077f394(param_1,&DAT_ffff0000907e8d1f,4);
      if (iVar1 == 0) {
        iVar1 = FUN_ffff00009073514c(param_2,uVar7);
        if (iVar1 != 0) {
          pcVar4 = s_write_gpt_failed__ffff0000907f0d5f;
          goto LAB_ffff00009070e5c4;
        }
        goto LAB_ffff00009070e55c;
      }
      lVar3 = FUN_ffff00009077c028(param_1);
      if ((lVar3 == 0) && (lVar3 = FUN_ffff00009077bf18(param_1), lVar3 == 0)) {
        pcVar4 = s_partition_does_not_exist_ffff0000907fe9ff;
LAB_ffff00009070e5c4:
        FUN_ffff000090709c9c(pcVar4);
        return;
      }
      if (DAT_ffff00009083dfbc == '\x01') {
        lVar8 = 0;
LAB_ffff00009070e4d0:
        iVar1 = FUN_ffff00009077f0ac(param_1,lVar8 + -0xffff6f7c2344);
        if (iVar1 != 0) goto code_r0xffff00009070e4e0;
        if (param_3 == 0) {
          uVar2 = 0;
        }
        else {
          uVar2 = 0xffffffff;
          pbVar6 = param_2;
          uVar5 = param_3;
          do {
            uVar5 = uVar5 - 1;
            uVar2 = *(uint *)(&DAT_ffff000090804028 + ((ulong)(*pbVar6 ^ uVar2) & 0xff) * 4) ^
                    uVar2 >> 8;
            pbVar6 = pbVar6 + 1;
          } while (uVar5 != 0);
          uVar2 = ~uVar2;
        }
        if (uVar2 != *(uint *)(&DAT_ffff00009083dcd0 + lVar8)) {
          FUN_ffff000090709c9c(s_check_crc_failed_ffff0000907d3e6c);
          FUN_ffff00009077bfac(lVar3);
          return;
        }
LAB_ffff00009070e5f4:
        FUN_ffff00009077e11c(s_CRC_verification_pass_ffff0000907f3ff0);
      }
      iVar1 = FUN_ffff00009077f394(param_1,s_preloader_ffff0000907d7473,9);
      if (iVar1 == 0) {
        iVar1 = FUN_ffff00009070cacc(param_2,&local_44);
        if (iVar1 != 0) {
          FUN_ffff000090709c9c(s_process_preloader_fail__ffff0000907f26e4);
          return;
        }
        iVar1 = thunk_FUN_ffff0000907aa2e4();
        if (iVar1 == 0) {
LAB_ffff00009070e754:
          iVar1 = FUN_ffff000090788e40(param_1,param_2,local_44);
          param_3 = local_44;
          if (iVar1 == 0) goto LAB_ffff00009070e618;
          pcVar4 = s_Image_platform_not_match_with_de_ffff0000907d0a7f;
        }
        else {
          iVar1 = FUN_ffff00009077c210(lVar3,0x102,&local_48);
          if (iVar1 == 0) {
            if (local_48 == 2) {
              lVar8 = 0x1000;
            }
            else {
              if (local_48 != 1) {
                pcVar4 = s_unsupport_flash_type_ffff0000907e0afa;
                goto LAB_ffff00009070e784;
              }
              lVar8 = 0x800;
            }
            iVar1 = FUN_ffff000090788c94(param_2 + lVar8,local_44 - (int)lVar8);
            if (iVar1 == 0) goto LAB_ffff00009070e754;
            pcVar4 = s_Preloader_auth_failed__ffff0000907dbfae;
          }
          else {
            FUN_ffff00009077e11c(s_bio_get_bootdev_type_fialed_ffff0000907f939d);
            pcVar4 = s_bio_get_bootdev_type_fialed_ffff0000907edaef;
          }
        }
      }
      else {
LAB_ffff00009070e618:
        if (*(long *)(lVar3 + 0x20) < (long)(ulong)(param_3 + 0x1ff & 0xfffffe00)) {
          pcVar4 = s_data_size_is_larger_than_partiti_ffff000090800416;
        }
        else {
          uVar2 = FUN_ffff00009077c144(lVar3,param_2,0,param_3);
          if (local_44 == uVar2) {
            if ((*(char *)(lVar3 + 0x50) == '\0') &&
               (iVar1 = FUN_ffff0000907809c4(*(undefined8 *)(lVar3 + 0x18),0), 0 < iVar1)) {
              FUN_ffff00009077cb0c();
            }
            FUN_ffff000090709cdc(&DAT_ffff0000907df338);
            goto LAB_ffff00009070e788;
          }
          pcVar4 = s_flash_write_failure_ffff0000907d0aa5;
        }
      }
LAB_ffff00009070e784:
      FUN_ffff000090709c9c(pcVar4);
LAB_ffff00009070e788:
      FUN_ffff00009077bfac(lVar3);
      return;
    }
    param_2[uVar7] = 0;
    iVar1 = FUN_ffff00009070db2c(param_2);
    if (iVar1 != 0) {
      pcVar4 = s_update_sparse_crc_list_failed_ffff0000907fb0e5;
      goto LAB_ffff00009070e5c4;
    }
    pcVar4 = s_loaded_sparsecrclist_ffff0000907edad9;
  }
  FUN_ffff00009077e11c(pcVar4);
LAB_ffff00009070e55c:
  FUN_ffff000090709cdc(&DAT_ffff0000907df338);
  return;
code_r0xffff00009070e4e0:
  lVar8 = lVar8 + 0x18;
  if (lVar8 == 0x300) goto LAB_ffff00009070e5f4;
  goto LAB_ffff00009070e4d0;
}


