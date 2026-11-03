// ffff00009070e3ac  size=904  FUN_ffff00009070e3ac

void FUN_ffff00009070e3ac(undefined8 param_1,byte *param_2,uint param_3)

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
  iVar1 = FUN_ffff00009077ede4(param_1,s_crclist_ffff0000907f2550);
  if (iVar1 == 0) {
    param_2[uVar7] = 0;
    iVar1 = FUN_ffff00009070dee0(param_2);
    if (iVar1 != 0) {
      pcVar4 = s_update_crc_list_failed_ffff000090800257;
      goto LAB_ffff00009070e54c;
    }
    pcVar4 = s_loaded_crclist_ffff0000907ea56c;
  }
  else {
    iVar1 = FUN_ffff00009077ede4(param_1,s_sparsecrclist_ffff0000907f91f3);
    if (iVar1 != 0) {
      iVar1 = FUN_ffff00009077f0cc(param_1,&DAT_ffff0000907e8bdb,4);
      if (iVar1 == 0) {
        iVar1 = FUN_ffff000090734eb4(param_2,uVar7);
        if (iVar1 != 0) {
          pcVar4 = s_write_gpt_failed__ffff0000907f0bee;
          goto LAB_ffff00009070e54c;
        }
        goto LAB_ffff00009070e4e4;
      }
      lVar3 = FUN_ffff00009077bd60(param_1);
      if ((lVar3 == 0) && (lVar3 = FUN_ffff00009077bc50(param_1), lVar3 == 0)) {
        pcVar4 = s_partition_does_not_exist_ffff0000907fe857;
LAB_ffff00009070e54c:
        FUN_ffff000090709d10(pcVar4);
        return;
      }
      if (DAT_ffff00009083df84 == '\x01') {
        lVar8 = 0;
LAB_ffff00009070e458:
        iVar1 = FUN_ffff00009077ede4(param_1,lVar8 + -0xffff6f7c237c);
        if (iVar1 != 0) goto code_r0xffff00009070e468;
        if (param_3 == 0) {
          uVar2 = 0;
        }
        else {
          uVar2 = 0xffffffff;
          pbVar6 = param_2;
          uVar5 = param_3;
          do {
            uVar5 = uVar5 - 1;
            uVar2 = *(uint *)(&DAT_ffff000090803e00 + ((ulong)(*pbVar6 ^ uVar2) & 0xff) * 4) ^
                    uVar2 >> 8;
            pbVar6 = pbVar6 + 1;
          } while (uVar5 != 0);
          uVar2 = ~uVar2;
        }
        if (uVar2 != *(uint *)(&DAT_ffff00009083dc98 + lVar8)) {
          FUN_ffff000090709d10(s_check_crc_failed_ffff0000907d3e2f);
          FUN_ffff00009077bce4(lVar3);
          return;
        }
LAB_ffff00009070e57c:
        FUN_ffff00009077de54(s_CRC_verification_pass_ffff0000907f3e64);
      }
      iVar1 = FUN_ffff00009077f0cc(param_1,s_preloader_ffff0000907d73dd,9);
      if (iVar1 == 0) {
        iVar1 = FUN_ffff00009070ca54(param_2,&local_44);
        if (iVar1 != 0) {
          FUN_ffff000090709d10(s_process_preloader_fail__ffff0000907f2558);
          return;
        }
        iVar1 = thunk_FUN_ffff0000907aa024();
        if (iVar1 == 0) {
LAB_ffff00009070e6dc:
          iVar1 = FUN_ffff000090788b80(param_1,param_2,local_44);
          param_3 = local_44;
          if (iVar1 == 0) goto LAB_ffff00009070e5a0;
          pcVar4 = s_Image_platform_not_match_with_de_ffff0000907d0a68;
        }
        else {
          iVar1 = FUN_ffff00009077bf48(lVar3,0x102,&local_48);
          if (iVar1 == 0) {
            if (local_48 == 2) {
              lVar8 = 0x1000;
            }
            else {
              if (local_48 != 1) {
                pcVar4 = s_unsupport_flash_type_ffff0000907e0a57;
                goto LAB_ffff00009070e70c;
              }
              lVar8 = 0x800;
            }
            iVar1 = FUN_ffff0000907889d4(param_2 + lVar8,local_44 - (int)lVar8);
            if (iVar1 == 0) goto LAB_ffff00009070e6dc;
            pcVar4 = s_Preloader_auth_failed__ffff0000907dbea3;
          }
          else {
            FUN_ffff00009077de54(s_bio_get_bootdev_type_fialed_ffff0000907f9201);
            pcVar4 = s_bio_get_bootdev_type_fialed_ffff0000907ed97e;
          }
        }
      }
      else {
LAB_ffff00009070e5a0:
        if (*(long *)(lVar3 + 0x20) < (long)(ulong)(param_3 + 0x1ff & 0xfffffe00)) {
          pcVar4 = s_data_size_is_larger_than_partiti_ffff00009080026e;
        }
        else {
          uVar2 = FUN_ffff00009077be7c(lVar3,param_2,0,param_3);
          if (local_44 == uVar2) {
            if ((*(char *)(lVar3 + 0x50) == '\0') &&
               (iVar1 = FUN_ffff0000907806fc(*(undefined8 *)(lVar3 + 0x18),0), 0 < iVar1)) {
              FUN_ffff00009077c844();
            }
            FUN_ffff000090709d50(&DAT_ffff0000907df295);
            goto LAB_ffff00009070e710;
          }
          pcVar4 = s_flash_write_failure_ffff0000907d0a8e;
        }
      }
LAB_ffff00009070e70c:
      FUN_ffff000090709d10(pcVar4);
LAB_ffff00009070e710:
      FUN_ffff00009077bce4(lVar3);
      return;
    }
    param_2[uVar7] = 0;
    iVar1 = FUN_ffff00009070dab4(param_2);
    if (iVar1 != 0) {
      pcVar4 = s_update_sparse_crc_list_failed_ffff0000907faf49;
      goto LAB_ffff00009070e54c;
    }
    pcVar4 = s_loaded_sparsecrclist_ffff0000907ed968;
  }
  FUN_ffff00009077de54(pcVar4);
LAB_ffff00009070e4e4:
  FUN_ffff000090709d50(&DAT_ffff0000907df295);
  return;
code_r0xffff00009070e468:
  lVar8 = lVar8 + 0x18;
  if (lVar8 == 0x300) goto LAB_ffff00009070e57c;
  goto LAB_ffff00009070e458;
}


