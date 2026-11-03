// ffff00009074a43c  size=132  FUN_ffff00009074a43c

undefined8 FUN_ffff00009074a43c(undefined8 param_1,undefined8 param_2)

{
  uint uVar1;
  int iVar2;
  undefined8 uVar3;
  
  uVar1 = FUN_ffff00009074b22c(0x1e);
  uVar1 = uVar1 & 0xff;
  if (0x13 < uVar1) {
    uVar1 = 0x14;
  }
  iVar2 = FUN_ffff00009077f6fc
                    (param_1,param_2,&DAT_ffff0000907dbf7c,
                     (&PTR_s_MT6991_ENG__ffff00009081ab48)[(ulong)uVar1 * 2]);
  if (iVar2 < 0) {
    FUN_ffff00009077e11c(s_Fail_to_copy_model_name__d__ffff0000907db1dd,iVar2);
    uVar3 = 0xffffffff;
  }
  else {
    uVar3 = 0;
  }
  return uVar3;
}


