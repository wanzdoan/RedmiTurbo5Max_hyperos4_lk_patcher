
void FUN_ffff0000907600a0(undefined8 param_1,undefined4 param_2)

{
  ulong *puVar1;
  ulong uVar2;
  undefined4 uVar3;
  ulong local_30;
  ulong local_28;
  
  FUN_ffff00009077d514(8);
  uVar3 = FUN_ffff00009076eb40(param_1,param_2,s_superboost_ffff0000907f8b11);
  local_28 = 0;
  FUN_ffff0000907358b8(s_super_ffff0000907e5b8c,&local_28);
  puVar1 = (ulong *)(local_28 + 0x28);
  uVar2 = (*(ulong *)(local_28 + 0x20) & 0xff00ff00ff00ff00) >> 8 |
          (*(ulong *)(local_28 + 0x20) & 0xff00ff00ff00ff) << 8;
  uVar2 = (uVar2 & 0xffff0000ffff0000) >> 0x10 | (uVar2 & 0xffff0000ffff) << 0x10;
  local_28 = uVar2 >> 0x20 | uVar2 << 0x20;
  uVar2 = (*puVar1 & 0xff00ff00ff00ff00) >> 8 | (*puVar1 & 0xff00ff00ff00ff) << 8;
  uVar2 = (uVar2 & 0xffff0000ffff0000) >> 0x10 | (uVar2 & 0xffff0000ffff) << 0x10;
  local_30 = uVar2 >> 0x20 | uVar2 << 0x20;
  FUN_ffff00009076e6d4(param_1,uVar3,s_super_start_lba_ffff0000907e9cdb,&local_28,8);
  FUN_ffff00009076e6d4(param_1,uVar3,s_super_end_lba_ffff0000907d3566,&local_30,8);
  return;
}


