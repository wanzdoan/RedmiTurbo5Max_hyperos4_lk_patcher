
int FUN_ffff0000be06eefc(void)

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
  
  DAT_ffff0000be1b4938 = -0xffffe97f0000;
  DAT_ffff0000be1b4940 = 0xffff0000168b0000;
  DAT_ffff0000be1b4948 = 0xffff000016800000;
  DAT_ffff0000be1b4950 = 1;
  DAT_ffff0000be1b4958 = FUN_ffff0000be05f204(0x80,0xc00);
  if (DAT_ffff0000be1b4958 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_common_ufs_ufs_ffff0000be0a8870,0xb6,
               s_hba_>ucdl_base_addr_ffff0000be0a9a77);
  }
  DAT_ffff0000be1b4968 = FUN_ffff0000be05d228();
  puVar12 = &DAT_ffff0000be1b4958;
  if ((DAT_ffff0000be1b4968 & 0x7f) == 0) {
    DAT_ffff0000be1b4960 = FUN_ffff0000be05f204(0x40,0x40);
    if (DAT_ffff0000be1b4960 == 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee98
                (s_platform_mediatek_common_ufs_ufs_ffff0000be0a8870,199,
                 s_hba_>utrdl_base_addr_ffff0000be0adfbc);
    }
    DAT_ffff0000be1b4970 = FUN_ffff0000be05d228();
    if ((DAT_ffff0000be1b4970 & 0x3f) == 0) {
      DAT_ffff0000be1b4ab0 = (byte)((uint)DAT_ffff0000be1b4960 >> 5) & 0x1f;
      DAT_ffff0000be1b49b0 = FUN_ffff0000be05f1bc(0x48);
      uVar4 = DAT_ffff0000be1b4968;
      lVar3 = DAT_ffff0000be1b4958;
      if (DAT_ffff0000be1b49b0 == 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000be05ee98
                  (s_platform_mediatek_common_ufs_ufs_ffff0000be0a8870,0xe2,
                   s_hba_>lrb_ffff0000be0c37a2);
      }
      uVar10 = (ulong)DAT_ffff0000be1b4950;
      if (0 < (int)DAT_ffff0000be1b4950) {
        uVar8 = 0;
        plVar11 = (long *)(DAT_ffff0000be1b49b0 + 0x10);
        lVar9 = DAT_ffff0000be1b4960;
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
      iVar5 = FUN_ffff0000be06f39c(&DAT_ffff0000be1b4938);
      if (iVar5 != 0) {
LAB_ffff0000be06f06c:
        FUN_ffff0000be05fd98(s__UFS__err__Host_controller_enabl_ffff0000be0af085);
        return -1;
      }
      DAT_ffff0000be1b4980 = 0;
      DAT_ffff0000be1b4be8 = *(uint *)(DAT_ffff0000be1b4938 + 0x2240);
      if ((DAT_ffff0000be1b4be8 | 0x1000000) >> 0x18 == 1) {
        DAT_ffff0000be1b4be8 = DAT_ffff0000be1b4be8 & 0xffffff | 0x10000000;
      }
      iVar5 = FUN_ffff0000be06f4f4(&DAT_ffff0000be1b4938);
      if (iVar5 == 0) {
LAB_ffff0000be06f1d8:
        uVar7 = FUN_ffff0000be04c784();
        DAT_ffff0000be1b4be4 = (int)uVar7;
        if (DAT_ffff0000be1b4be4 == -1) {
          FUN_ffff0000be05fd98(s__UFS__UFS_IRQ_ID_is_not_define_ffff0000be0b6f5d);
        }
        else {
          FUN_ffff0000be021e1c(uVar7,1);
          FUN_ffff0000be021e10(DAT_ffff0000be1b4be4,0);
          FUN_ffff0000be01a4c4(&DAT_ffff0000be1b4900,0,1);
          FUN_ffff0000be018fd4(DAT_ffff0000be1b4be4,FUN_ffff0000be072060,0);
          FUN_ffff0000be0194d8(DAT_ffff0000be1b4be4);
          DAT_ffff0000be1b4be0 = DAT_ffff0000be1b4be0 | 2;
        }
        FUN_ffff0000be06edf4();
        local_24[0] = '\x01';
        iVar5 = FUN_ffff0000be0742fc(&DAT_ffff0000be1b4938,2,local_24);
        if (iVar5 == 0) {
          puVar2 = &DAT_ffff0000be0b5d0a;
          if (local_24[0] != '\0') {
            puVar2 = &DAT_ffff0000be0ae340;
          }
          FUN_ffff0000be05fd98(s__UFS__iSE_RPMB_key_s_programmed__ffff0000be0a516c,puVar2);
          DAT_ffff0000be1b4ae4 = 2;
          if (local_24[0] == '\0') {
            DAT_ffff0000be1b4ae4 = 3;
          }
        }
        else {
          FUN_ffff0000be05fd98(s__UFS__iSE_RPMB_key_status_unknow_ffff0000be0b4ab3);
          DAT_ffff0000be1b4ae4 = 1;
        }
        local_24[0] = '\x01';
        iVar5 = FUN_ffff0000be0742fc(&DAT_ffff0000be1b4938,3,local_24);
        if (iVar5 == 0) {
          puVar2 = &DAT_ffff0000be0b5d0a;
          if (local_24[0] != '\0') {
            puVar2 = &DAT_ffff0000be0ae340;
          }
          FUN_ffff0000be05fd98(s__UFS__MTEE_RPMB_key_s_programmed_ffff0000be0bc7f9,puVar2);
          DAT_ffff0000be1b4ae8 = 2;
          if (local_24[0] == '\0') {
            DAT_ffff0000be1b4ae8 = 3;
          }
        }
        else {
          FUN_ffff0000be05fd98(s__UFS__MTEE_RPMB_key_status_unkno_ffff0000be0c0116);
          DAT_ffff0000be1b4ae8 = 1;
        }
        return 0;
      }
      if (iVar5 == -1) {
        iVar5 = FUN_ffff0000be06f39c(&DAT_ffff0000be1b4938);
        if (iVar5 != 0) goto LAB_ffff0000be06f06c;
        DAT_ffff0000be1b4980 = 0;
        DAT_ffff0000be1b4be8 = *(uint *)(DAT_ffff0000be1b4938 + 0x2240);
        if ((DAT_ffff0000be1b4be8 | 0x1000000) >> 0x18 == 1) {
          DAT_ffff0000be1b4be8 = DAT_ffff0000be1b4be8 & 0xffffff | 0x10000000;
        }
        iVar5 = FUN_ffff0000be06f4f4(&DAT_ffff0000be1b4938);
        if (iVar5 == 0) goto LAB_ffff0000be06f1d8;
        if (iVar5 == -1) {
          iVar5 = FUN_ffff0000be06f39c(&DAT_ffff0000be1b4938);
          if (iVar5 != 0) goto LAB_ffff0000be06f06c;
          DAT_ffff0000be1b4980 = 0;
          DAT_ffff0000be1b4be8 = *(uint *)(DAT_ffff0000be1b4938 + 0x2240);
          if ((DAT_ffff0000be1b4be8 | 0x1000000) >> 0x18 == 1) {
            DAT_ffff0000be1b4be8 = DAT_ffff0000be1b4be8 & 0xffffff | 0x10000000;
          }
          iVar5 = FUN_ffff0000be06f4f4(&DAT_ffff0000be1b4938);
          if (iVar5 == 0) goto LAB_ffff0000be06f1d8;
          if (iVar5 == -1) {
            iVar5 = FUN_ffff0000be06f39c(&DAT_ffff0000be1b4938);
            if (iVar5 != 0) goto LAB_ffff0000be06f06c;
            DAT_ffff0000be1b4980 = 0;
            DAT_ffff0000be1b4be8 = *(uint *)(DAT_ffff0000be1b4938 + 0x2240);
            if ((DAT_ffff0000be1b4be8 | 0x1000000) >> 0x18 == 1) {
              DAT_ffff0000be1b4be8 = DAT_ffff0000be1b4be8 & 0xffffff | 0x10000000;
            }
            iVar5 = FUN_ffff0000be06f4f4(&DAT_ffff0000be1b4938);
            if (iVar5 == 0) goto LAB_ffff0000be06f1d8;
            if (iVar5 == -1) {
              FUN_ffff0000be05fd98(s__UFS__err__retry_number_exceeded_ffff0000be0a9a27);
              return -1;
            }
          }
        }
      }
      FUN_ffff0000be05fd98(s__UFS__err__ufshcd_probe_hba_fail_ffff0000be0adf29);
      return iVar5;
    }
    puVar12 = &DAT_ffff0000be1b4960;
    thunk_FUN_ffff0000be09937c(DAT_ffff0000be1b4958);
  }
  thunk_FUN_ffff0000be09937c(*puVar12);
  return -1;
}


