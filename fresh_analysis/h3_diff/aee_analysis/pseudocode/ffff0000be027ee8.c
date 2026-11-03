
undefined4 FUN_ffff0000be027ee8(int *param_1,uint param_2)

{
  undefined4 uVar1;
  long lVar2;
  ulong uVar3;
  ulong uVar4;
  ulong uVar5;
  uint uVar6;
  ulong uVar7;
  
  if (param_1 == (int *)0x0) {
    uVar1 = 0xffffffea;
  }
  else {
    uVar4 = (ulong)param_2;
    lVar2 = FUN_ffff0000be05de5c(s_bootdevice_ffff0000be0a45a8);
    if (lVar2 == 0) {
      FUN_ffff0000be05fd98
                (s_Partition___s__is_not_exist__ffff0000be0b63a1,s_bootdevice_ffff0000be0a45a8);
      uVar1 = 0xffffffed;
    }
    else {
      if ((*param_1 == 0x20494645) || (param_1[1] == 0x54524150)) {
        uVar6 = *(uint *)(lVar2 + 0x28);
        uVar5 = 0;
      }
      else {
        uVar5 = *(ulong *)(lVar2 + 0x28) & 0xffffffff;
        uVar6 = (int)*(ulong *)(lVar2 + 0x28) << 1;
      }
      FUN_ffff0000be05def0();
      uVar7 = (ulong)uVar6;
      uVar3 = (ulong)*(uint *)((long)param_1 + uVar5 + 0x50);
      if ((uVar7 + uVar3 * 0x80 == uVar4) ||
         (FUN_ffff0000be05fd98(s__GPT_Update__Wrong_Content_Binar_ffff0000be0c0963,uVar3,uVar4),
         param_2 < 0x7001)) {
        FUN_ffff0000be05fd98(s__GPT_Update__update_pgt_with_gpt_ffff0000be0ab5d5,uVar7);
        uVar1 = FUN_ffff0000be02886c((long)param_1 + uVar5,(long)param_1 + uVar7);
        FUN_ffff0000be028940();
      }
      else {
        FUN_ffff0000be05fd98(s__GPT_Update__Current_size__u_is_g_ffff0000be0b8a37,uVar4,0x7000);
        uVar1 = 0xffffffea;
      }
    }
  }
  return uVar1;
}


