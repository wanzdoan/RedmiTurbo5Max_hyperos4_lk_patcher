// ffff00009074a0f8  size=172  FUN_ffff00009074a0f8

undefined8 FUN_ffff00009074a0f8(undefined8 param_1,undefined8 param_2)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  int iVar4;
  
  uVar2 = FUN_ffff00009074af94(0x1e);
  uVar2 = uVar2 & 0xff;
  uVar1 = uVar2;
  if (0x13 < uVar2) {
    uVar1 = 0x14;
  }
  uVar3 = FUN_ffff00009074af94(0x1d);
  FUN_ffff00009077de54(s_proj_code__0x_x_ffff0000907efdad,uVar3 & 0x3fff);
  FUN_ffff00009077de54(s__LK__segment___0x_x_ffff0000907f4c3d,uVar2);
  iVar4 = FUN_ffff00009077f434
                    (param_1,param_2,&DAT_ffff0000907dbe71,
                     (&PTR_s_MT6991_ENG__ffff00009081ab20)[(ulong)uVar1 * 2]);
  if (iVar4 < 0) {
    FUN_ffff00009077de54(s_Fail_to_copy_model_name__d__ffff0000907db0d2,iVar4);
  }
  return 0;
}


