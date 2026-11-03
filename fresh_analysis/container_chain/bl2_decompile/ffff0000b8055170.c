
int FUN_ffff0000b8055170(void)

{
  long lVar1;
  undefined *puVar2;
  long lVar3;
  ulong uVar4;
  int iVar5;
  ulong uVar6;
  undefined8 uVar7;
  ulong uVar8;
  long lVar9;
  ulong uVar10;
  long *plVar11;
  undefined8 *puVar12;
  char local_24 [4];
  
  DAT_ffff0000b81b6678 = -0xffffe97f0000;
  DAT_ffff0000b81b6680 = 0xffff0000168b0000;
  DAT_ffff0000b81b6688 = 0xffff000016800000;
  DAT_ffff0000b81b6690 = 1;
  DAT_ffff0000b81b6698 = FUN_ffff0000b804bd14(0x80,0xc00);
  if (DAT_ffff0000b81b6698 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_common_ufs_ufs_ffff0000b80895e3,0xb6,
               s_hba_>ucdl_base_addr_ffff0000b808a35b);
  }
  DAT_ffff0000b81b66a8 = FUN_ffff0000b8049ec8();
  puVar12 = &DAT_ffff0000b81b6698;
  if ((DAT_ffff0000b81b66a8 & 0x7f) == 0) {
    DAT_ffff0000b81b66a0 = FUN_ffff0000b804bd14(0x40,0x40);
    if (DAT_ffff0000b81b66a0 == 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b830
                (s_platform_mediatek_common_ufs_ufs_ffff0000b80895e3,199,
                 s_hba_>utrdl_base_addr_ffff0000b808d7b0);
    }
    DAT_ffff0000b81b66b0 = FUN_ffff0000b8049ec8();
    if ((DAT_ffff0000b81b66b0 & 0x3f) == 0) {
      DAT_ffff0000b81b67f0 = (byte)((uint)DAT_ffff0000b81b66a0 >> 5) & 0x1f;
      DAT_ffff0000b81b66f0 = FUN_ffff0000b804bccc(0x48);
      uVar4 = DAT_ffff0000b81b66a8;
      lVar3 = DAT_ffff0000b81b6698;
      if (DAT_ffff0000b81b66f0 == 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000b804b830
                  (s_platform_mediatek_common_ufs_ufs_ffff0000b80895e3,0xe2,
                   s_hba_>lrb_ffff0000b809ef22);
      }
      uVar10 = (ulong)DAT_ffff0000b81b6690;
      if (0 < (int)DAT_ffff0000b81b6690) {
        uVar8 = 0;
        plVar11 = (long *)(DAT_ffff0000b81b66f0 + 0x10);
        lVar9 = DAT_ffff0000b81b66a0;
        do {
          uVar6 = uVar8 & 0xfffffc00;
          lVar1 = lVar3 + uVar8;
          *(undefined4 *)(lVar9 + 0x18) = 0x800080;
          uVar8 = uVar8 + 0xc00;
          *(undefined2 *)(lVar9 + 0x1e) = 0x100;
          *plVar11 = lVar1 + 0x200;
          plVar11[1] = lVar1 + 0x400;
          *(ulong *)(lVar9 + 0x10) = uVar6 + uVar4;
          plVar11[-2] = lVar9;
          plVar11[-1] = lVar1;
          lVar9 = lVar9 + 0x20;
          plVar11 = plVar11 + 9;
        } while (uVar10 * 0xc00 - uVar8 != 0);
      }
      iVar5 = FUN_ffff0000b8055610(&DAT_ffff0000b81b6678);
      if (iVar5 != 0) {
LAB_ffff0000b80552e0:
        FUN_ffff0000b804c33c(s__UFS__err__Host_controller_enabl_ffff0000b808e594);
        return -1;
      }
      DAT_ffff0000b81b66c0 = 0;
      DAT_ffff0000b81b6928 = *(uint *)(DAT_ffff0000b81b6678 + 0x2240);
      if ((DAT_ffff0000b81b6928 | 0x1000000) >> 0x18 == 1) {
        DAT_ffff0000b81b6928 = DAT_ffff0000b81b6928 & 0xffffff | 0x10000000;
      }
      iVar5 = FUN_ffff0000b8055768(&DAT_ffff0000b81b6678);
      if (iVar5 == 0) {
LAB_ffff0000b805544c:
        uVar7 = FUN_ffff0000b80436dc();
        DAT_ffff0000b81b6924 = (int)uVar7;
        if (DAT_ffff0000b81b6924 == -1) {
          FUN_ffff0000b804c33c(s__UFS__UFS_IRQ_ID_is_not_define_ffff0000b8094db5);
        }
        else {
          FUN_ffff0000b8017f0c(uVar7,1);
          FUN_ffff0000b8017f00(DAT_ffff0000b81b6924,0);
          FUN_ffff0000b800abd0(&DAT_ffff0000b81b6640,0,1);
          FUN_ffff0000b80096f4(DAT_ffff0000b81b6924,FUN_ffff0000b80583f0,0);
          FUN_ffff0000b8009be4(DAT_ffff0000b81b6924);
          DAT_ffff0000b81b6920 = DAT_ffff0000b81b6920 | 2;
        }
        FUN_ffff0000b8055068();
        local_24[0] = '\x01';
        iVar5 = FUN_ffff0000b8059f6c(&DAT_ffff0000b81b6678,2,local_24);
        if (iVar5 == 0) {
          puVar2 = &DAT_ffff0000b8093dc6;
          if (local_24[0] != '\0') {
            puVar2 = &DAT_ffff0000b808dbfe;
          }
          FUN_ffff0000b804c33c(s__UFS__iSE_RPMB_key_s_programmed__ffff0000b8086ade,puVar2);
          DAT_ffff0000b81b6824 = 2;
          if (local_24[0] == '\0') {
            DAT_ffff0000b81b6824 = 3;
          }
        }
        else {
          FUN_ffff0000b804c33c(s__UFS__iSE_RPMB_key_status_unknow_ffff0000b8092d96);
          DAT_ffff0000b81b6824 = 1;
        }
        local_24[0] = '\x01';
        iVar5 = FUN_ffff0000b8059f6c(&DAT_ffff0000b81b6678,3,local_24);
        if (iVar5 == 0) {
          puVar2 = &DAT_ffff0000b8093dc6;
          if (local_24[0] != '\0') {
            puVar2 = &DAT_ffff0000b808dbfe;
          }
          FUN_ffff0000b804c33c(s__UFS__MTEE_RPMB_key_s_programmed_ffff0000b809947c,puVar2);
          DAT_ffff0000b81b6828 = 2;
          if (local_24[0] == '\0') {
            DAT_ffff0000b81b6828 = 3;
          }
        }
        else {
          FUN_ffff0000b804c33c(s__UFS__MTEE_RPMB_key_status_unkno_ffff0000b809bf56);
          DAT_ffff0000b81b6828 = 1;
        }
        return 0;
      }
      if (iVar5 == -1) {
        iVar5 = FUN_ffff0000b8055610(&DAT_ffff0000b81b6678);
        if (iVar5 != 0) goto LAB_ffff0000b80552e0;
        DAT_ffff0000b81b66c0 = 0;
        DAT_ffff0000b81b6928 = *(uint *)(DAT_ffff0000b81b6678 + 0x2240);
        if ((DAT_ffff0000b81b6928 | 0x1000000) >> 0x18 == 1) {
          DAT_ffff0000b81b6928 = DAT_ffff0000b81b6928 & 0xffffff | 0x10000000;
        }
        iVar5 = FUN_ffff0000b8055768(&DAT_ffff0000b81b6678);
        if (iVar5 == 0) goto LAB_ffff0000b805544c;
        if (iVar5 == -1) {
          iVar5 = FUN_ffff0000b8055610(&DAT_ffff0000b81b6678);
          if (iVar5 != 0) goto LAB_ffff0000b80552e0;
          DAT_ffff0000b81b66c0 = 0;
          DAT_ffff0000b81b6928 = *(uint *)(DAT_ffff0000b81b6678 + 0x2240);
          if ((DAT_ffff0000b81b6928 | 0x1000000) >> 0x18 == 1) {
            DAT_ffff0000b81b6928 = DAT_ffff0000b81b6928 & 0xffffff | 0x10000000;
          }
          iVar5 = FUN_ffff0000b8055768(&DAT_ffff0000b81b6678);
          if (iVar5 == 0) goto LAB_ffff0000b805544c;
          if (iVar5 == -1) {
            iVar5 = FUN_ffff0000b8055610(&DAT_ffff0000b81b6678);
            if (iVar5 != 0) goto LAB_ffff0000b80552e0;
            DAT_ffff0000b81b66c0 = 0;
            DAT_ffff0000b81b6928 = *(uint *)(DAT_ffff0000b81b6678 + 0x2240);
            if ((DAT_ffff0000b81b6928 | 0x1000000) >> 0x18 == 1) {
              DAT_ffff0000b81b6928 = DAT_ffff0000b81b6928 & 0xffffff | 0x10000000;
            }
            iVar5 = FUN_ffff0000b8055768(&DAT_ffff0000b81b6678);
            if (iVar5 == 0) goto LAB_ffff0000b805544c;
            if (iVar5 == -1) {
              FUN_ffff0000b804c33c(s__UFS__err__retry_number_exceeded_ffff0000b808a30b);
              return -1;
            }
          }
        }
      }
      FUN_ffff0000b804c33c(s__UFS__err__ufshcd_probe_hba_fail_ffff0000b808d70e);
      return iVar5;
    }
    puVar12 = &DAT_ffff0000b81b66a0;
    thunk_FUN_ffff0000b8082558(DAT_ffff0000b81b6698);
  }
  thunk_FUN_ffff0000b8082558(*puVar12);
  return -1;
}


