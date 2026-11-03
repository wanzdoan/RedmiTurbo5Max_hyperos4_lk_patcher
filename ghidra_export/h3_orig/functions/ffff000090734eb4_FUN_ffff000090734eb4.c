// ffff000090734eb4  size=292  FUN_ffff000090734eb4

undefined4 FUN_ffff000090734eb4(int *param_1,uint param_2)

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
    lVar2 = FUN_ffff00009077bc50(s_bootdevice_ffff0000907d0ccc);
    if (lVar2 == 0) {
      FUN_ffff00009077de54
                (s_Partition___s__is_not_exist__ffff0000907ea8d7,s_bootdevice_ffff0000907d0ccc);
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
      FUN_ffff00009077bce4();
      uVar7 = (ulong)uVar6;
      uVar3 = (ulong)*(uint *)((long)param_1 + uVar5 + 0x50);
      if ((uVar7 + uVar3 * 0x80 == uVar4) ||
         (FUN_ffff00009077de54(s__GPT_Update__Wrong_Content_Binar_ffff0000907f9b34,uVar3,uVar4),
         param_2 < 0x7001)) {
        FUN_ffff00009077de54(s__GPT_Update__update_pgt_with_gpt_ffff0000907daaf8,uVar7);
        uVar1 = FUN_ffff000090735b74((long)param_1 + uVar5,(long)param_1 + uVar7);
        FUN_ffff000090735c48();
      }
      else {
        FUN_ffff00009077de54(s__GPT_Update__Current_size__u_is_g_ffff0000907ee12c,uVar4,0x7000);
        uVar1 = 0xffffffea;
      }
    }
  }
  return uVar1;
}


