// ffff00009074a1a4  size=132  FUN_ffff00009074a1a4

undefined8 FUN_ffff00009074a1a4(undefined8 param_1,undefined8 param_2)

{
  uint uVar1;
  int iVar2;
  undefined8 uVar3;
  
  uVar1 = FUN_ffff00009074af94(0x1e);
  uVar1 = uVar1 & 0xff;
  if (0x13 < uVar1) {
    uVar1 = 0x14;
  }
  iVar2 = FUN_ffff00009077f434
                    (param_1,param_2,&DAT_ffff0000907dbe71,
                     (&PTR_s_MT6991_ENG__ffff00009081ab28)[(ulong)uVar1 * 2]);
  if (iVar2 < 0) {
    FUN_ffff00009077de54(s_Fail_to_copy_model_name__d__ffff0000907db0d2,iVar2);
    uVar3 = 0xffffffff;
  }
  else {
    uVar3 = 0;
  }
  return uVar3;
}


