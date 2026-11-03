// ffff000090738c50  size=444  FUN_ffff000090738c50

void FUN_ffff000090738c50(void)

{
  int iVar1;
  long lVar2;
  long *plVar3;
  ulong uVar4;
  long lVar5;
  long local_80;
  long lStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  
  iVar1 = FUN_ffff00009078abf4();
  uVar4 = (ulong)(iVar1 + 0xfffU & 0xfffff000) + 0xffff & 0x1ffff0000;
  iVar1 = FUN_ffff00009071c708();
  if (iVar1 == 0) {
    lVar2 = FUN_ffff0000907198dc
                      (uVar4,0x1000000,0x90000000,0,0,s_reserve_memory_scp_share_ffff0000907e62dc);
  }
  else {
    lVar2 = FUN_ffff00009071a09c
                      (uVar4,0x1000000,0x250000000,0x2a0000000,0,0,
                       s_reserve_memory_scp_share_ffff0000907e62dc);
  }
  if (lVar2 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___SCP__mblock_reserve_fail_ffff0000907e4aa6,
               s_scp_sharedram_alloc_ffff00009080278a,0x281);
  }
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  plVar3 = (long *)FUN_ffff00009071a348(s_scp_aov_reserved_ffff0000907e7ae2,0);
  lVar5 = *plVar3;
  if (lVar5 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___SCP__aov_mblock_reserve_f_ffff0000907d92f4,
               s_scp_sharedram_alloc_ffff00009080278a,0x28b);
  }
  FUN_ffff00009071ebf4(0xc200040f,0,lVar2,uVar4,0,0,0,0,&local_60);
  FUN_ffff00009071ebf4(0xc200040f,6,lVar5,0x1000000,0,0,0,0,&local_60);
  lStack_78 = lVar2 + uVar4;
  uStack_68 = 0;
  local_70 = 0x1b;
  local_80 = lVar2;
  FUN_ffff00009078b0fc(&local_80);
  uStack_68 = 0;
  local_70 = 0x3c;
  local_80 = lVar5;
  lStack_78 = lVar5 + 0xe00000;
  FUN_ffff00009078b0fc(&local_80);
  lStack_78 = lVar5 + 0x1000000;
  uStack_68 = 0;
  local_70 = 0x3d;
  local_80 = lVar5 + 0xe00000;
  FUN_ffff00009078b0fc(&local_80);
  return;
}


