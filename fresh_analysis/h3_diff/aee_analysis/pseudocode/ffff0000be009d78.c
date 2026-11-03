
void FUN_ffff0000be009d78(void)

{
  undefined4 uVar1;
  uint uVar2;
  int iVar3;
  ulong uVar4;
  long lVar5;
  long *plVar6;
  long lVar7;
  long lVar8;
  undefined8 uVar9;
  long lVar10;
  long lVar11;
  char *pcVar12;
  
  FUN_ffff0000be035310(0xffff7f00,0xffff0000);
  FUN_ffff0000be03535c();
  FUN_ffff0000be035050(0,0);
  if ((*(uint *)(DAT_ffff0000be0f90d0 + 0xf8) & 0xff) < 10) {
    pcVar12 = (&PTR_s_NORMAL_BOOT_ffff0000be0d5ff8)[(char)*(uint *)(DAT_ffff0000be0f90d0 + 0xf8)];
  }
  else {
    pcVar12 = s_UNKNOWN_BOOT_ffff0000be0baa03;
  }
  uVar4 = FUN_ffff0000be00a070();
  FUN_ffff0000be0114f0
            (s_Kdump_triggerd_by___s___address__ffff0000be0c5ef3,pcVar12,0x80000000,uVar4 >> 0x14);
  uVar2 = FUN_ffff0000be0553a4(0xffffffff,DAT_ffff0000be0f90d0 + 8,0xe0);
  if (*(uint *)(DAT_ffff0000be0f90d0 + 0xe8) != ~uVar2) {
    FUN_ffff0000be0115fc(s_Control_block_machdesc_field_CRC_ffff0000be0a5588);
    return;
  }
  lVar5 = FUN_ffff0000be012768(DAT_ffff0000be0f90d0);
  lVar8 = DAT_ffff0000be0f90d0;
  if (lVar5 == 0) {
    pcVar12 = s__s__d__Cannot_allcate_memlist_cm_ffff0000be0bf2f9;
    uVar9 = 0xbe;
    goto LAB_ffff0000be009f98;
  }
  plVar6 = (long *)FUN_ffff0000be05f1bc(0xc0);
  if (plVar6 == (long *)0x0) {
LAB_ffff0000be009f7c:
    FUN_ffff0000be013088(lVar5);
    pcVar12 = s__s__d__Cannot_allcate_memlist_me_ffff0000be0ad246;
    uVar9 = 0xc6;
LAB_ffff0000be009f98:
    FUN_ffff0000be05fd98(pcVar12,s_mrdump_run_ffff0000be0a55be,uVar9);
    return;
  }
  lVar7 = FUN_ffff0000be05f1bc(0x3000);
  if (lVar7 == 0) {
    thunk_FUN_ffff0000be09937c(plVar6);
    goto LAB_ffff0000be009f7c;
  }
  FUN_ffff0000be060ae0(lVar7,0,0x3000);
  *plVar6 = lVar7;
  plVar6[1] = 0x3000;
  *(undefined4 *)(plVar6 + 2) = 0;
  plVar6[3] = 0;
  plVar6[4] = 0;
  *(undefined4 *)(plVar6 + 5) = 0;
  lVar7 = FUN_ffff0000be011ba8();
  plVar6[6] = lVar7;
  iVar3 = FUN_ffff0000be011bc8();
  *(undefined4 *)(plVar6 + 8) = 0;
  plVar6[9] = 0;
  plVar6[10] = 0;
  *(undefined4 *)(plVar6 + 0xb) = 0;
  plVar6[7] = (long)iVar3;
  plVar6[0xc] = 0x80000000;
  lVar7 = FUN_ffff0000be00a070();
  lVar10 = *(long *)(lVar8 + 0x98);
  plVar6[0xd] = lVar7;
  lVar7 = DAT_ffff0000be0f90d0;
  lVar11 = *(long *)(lVar8 + 0xa0);
  *(undefined4 *)(plVar6 + 0xe) = 1;
  uVar1 = *(undefined4 *)(lVar8 + 0xa8);
  plVar6[0xf] = lVar10;
  plVar6[0x10] = lVar11;
  plVar6[0x12] = 0;
  plVar6[0x13] = 0;
  *(undefined4 *)(plVar6 + 0x11) = uVar1;
  *(undefined4 *)(plVar6 + 0x14) = 0;
  plVar6[0x15] = 0;
  plVar6[0x16] = 0;
  *(undefined4 *)(plVar6 + 0x17) = 0;
  FUN_ffff0000be010638(lVar7,plVar6);
  switch(DAT_ffff0000be0f90d8) {
  case 1:
    uVar4 = FUN_ffff0000be0658b0();
    if (((uVar4 & 1) != 0) && (iVar3 = FUN_ffff0000be0093f8(), iVar3 < 0)) {
      FUN_ffff0000be05fd98
                (s__s__d__dump_expdb_fail_ffff0000be0ad209,s_mrdump_run_ffff0000be0a55be,0xd1);
    }
    break;
  case 2:
    break;
  case 3:
    lVar8 = FUN_ffff0000be01181c();
    if ((lVar8 != 0) &&
       (iVar3 = FUN_ffff0000be011e94(DAT_ffff0000be0f90d0,plVar6,lVar5,lVar8), iVar3 != 0)) {
      FUN_ffff0000be0115fc(s_mrdump_ext4_output_fail___d__ffff0000be0c17df,iVar3);
      thunk_FUN_ffff0000be09937c(lVar8);
    }
    break;
  default:
    pcVar12 = s_Unsupport_device_id__d_ffff0000be0a9f2e;
    iVar3 = DAT_ffff0000be0f90d8;
    goto LAB_ffff0000be00a028;
  case 5:
    iVar3 = FUN_ffff0000be00a284(DAT_ffff0000be0f90d0,plVar6,lVar5);
    if (iVar3 == 0) break;
    pcVar12 = s_mrdump_usb_output_fail___d__ffff0000be0baa10;
LAB_ffff0000be00a028:
    FUN_ffff0000be0115fc(pcVar12,iVar3);
  }
  thunk_FUN_ffff0000be09937c(*plVar6);
  thunk_FUN_ffff0000be09937c(plVar6);
  FUN_ffff0000be013088(lVar5);
  FUN_ffff0000be0357ec();
  FUN_ffff0000be0352a4();
  FUN_ffff0000be035050(0,0);
  return;
}


