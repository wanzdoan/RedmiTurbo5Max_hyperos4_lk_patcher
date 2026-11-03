
uint FUN_ffff0000be00a6b0(long param_1,long param_2,uint param_3)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  int iVar4;
  ulong uVar5;
  undefined8 uVar6;
  char *pcVar7;
  uint uVar8;
  
  *(int *)(param_1 + 0xfb40) = *(int *)(param_1 + 0xfb40) + param_3;
  if ((param_2 == 0) && (param_3 == 0)) {
    FUN_ffff0000be05ede4(10000);
    uVar5 = FUN_ffff0000be00ac04(param_1,*(undefined4 *)(param_1 + 0xfb44));
    if ((uVar5 & 1) == 0) {
      FUN_ffff0000be0115fc
                (s__s__connection_failed__error_idx_ffff0000be0bbac0,s_usb_write_cb_ffff0000be0c034f
                 ,*(undefined4 *)(param_1 + 0xfb44));
      return 0xffffffff;
    }
    param_1 = param_1 + 0x40;
    FUN_ffff0000be060ae0(param_1,0,0xfb00);
    iVar4 = FUN_ffff0000be0611dc(param_1,0xfb00,s_MRDUMP08_DONE_ffff0000be0b081e);
    if (iVar4 < 0) {
      pcVar7 = s_USB_Dump__Make_MRDUMP_END_Packet_ffff0000be0bf340;
    }
    else {
      uVar6 = FUN_ffff0000be060e5c(param_1);
      iVar4 = FUN_ffff0000be018ca4(param_1,uVar6);
      if (-1 < iVar4) {
        return 0;
      }
      pcVar7 = s_USB_Dump__Write_MRDUMP_ZLP_faile_ffff0000be0bbae7;
    }
  }
  else {
    if (param_2 == 0) {
      return 0xffffffff;
    }
    uVar5 = (ulong)*(uint *)(param_1 + 0xfb44);
    uVar8 = param_3;
    while( true ) {
      uVar3 = 0xfb00 - (int)uVar5;
      uVar1 = uVar3;
      if (uVar8 <= uVar3) {
        uVar1 = uVar8;
      }
      uVar2 = 0;
      if (uVar3 <= uVar8) {
        uVar2 = uVar8 - uVar3;
      }
      FUN_ffff0000be0607b0(param_1 + uVar5 + 0x40,param_2,uVar1);
      uVar1 = *(int *)(param_1 + 0xfb44) + uVar1;
      uVar5 = (ulong)uVar1;
      *(uint *)(param_1 + 0xfb44) = uVar1;
      if (uVar1 == 0xfb00) break;
LAB_ffff0000be00a7c0:
      if ((int)uVar2 < 0) goto LAB_ffff0000be00a81c;
      if (uVar8 <= uVar3) {
        return param_3;
      }
      param_2 = param_2 + (ulong)(uVar8 - uVar2);
      uVar8 = uVar2;
    }
    uVar5 = FUN_ffff0000be00ac04(param_1,0xfb00);
    if ((uVar5 & 1) != 0) {
      uVar5 = 0;
      *(undefined4 *)(param_1 + 0xfb44) = 0;
      goto LAB_ffff0000be00a7c0;
    }
    FUN_ffff0000be0115fc
              (s__s__connection_failed__error_idx_ffff0000be0bbac0,
               s_do_store_or_write_ffff0000be0bf36b,*(undefined4 *)(param_1 + 0xfb44));
LAB_ffff0000be00a81c:
    pcVar7 = s_USB_Dump__store_and_write_failed_ffff0000be0a68e2;
  }
  FUN_ffff0000be0115fc(pcVar7);
  return 0xffffffff;
}


