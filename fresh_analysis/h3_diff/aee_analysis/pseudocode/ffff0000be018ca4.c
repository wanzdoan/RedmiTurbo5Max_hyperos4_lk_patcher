
int FUN_ffff0000be018ca4(long param_1,uint param_2)

{
  long *plVar1;
  undefined8 uVar2;
  int iVar3;
  int iVar4;
  char *pcVar5;
  uint uVar6;
  
  iVar3 = FUN_ffff0000be0133d4();
  if (iVar3 == 3) {
LAB_ffff0000be018d8c:
    FUN_ffff0000be05fd98(s_ERROR_ERROR_ERROR_______ffff0000be0bbd50);
    FUN_ffff0000be0133e8(3);
    iVar3 = -1;
  }
  else {
    iVar3 = 0;
    plVar1 = DAT_ffff0000be108818;
    for (; DAT_ffff0000be108818 = plVar1, param_2 != 0; param_2 = param_2 - uVar6) {
      *plVar1 = param_1;
      uVar2 = DAT_ffff0000be108860;
      uVar6 = param_2;
      if (0x3fff < param_2) {
        uVar6 = 0x4000;
      }
      plVar1[2] = (long)&LAB_ffff0000be018ddc;
      *(uint *)(plVar1 + 1) = uVar6;
      iVar4 = FUN_ffff0000be0694f4(uVar2);
      if (iVar4 < 0) {
        pcVar5 = s__s___queue_failed_ffff0000be0ad4f8;
LAB_ffff0000be018d80:
        FUN_ffff0000be05fd98(pcVar5,s_usb_write_with_timeout_ffff0000be0bcf06);
        goto LAB_ffff0000be018d8c;
      }
      iVar4 = FUN_ffff0000be01a590(&DAT_ffff0000be108828,0xffffffff);
      if (iVar4 != 0) goto LAB_ffff0000be018d8c;
      if (DAT_ffff0000be108858 < 0) {
        pcVar5 = s__s___transaction_failed_ffff0000be0af65c;
        goto LAB_ffff0000be018d80;
      }
      uVar6 = *(uint *)(DAT_ffff0000be108818 + 1);
      iVar3 = uVar6 + iVar3;
      param_1 = param_1 + (ulong)uVar6;
      plVar1 = DAT_ffff0000be108818;
    }
  }
  return iVar3;
}


