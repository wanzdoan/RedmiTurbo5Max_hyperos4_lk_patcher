// ffff0000907389b8  size=444  FUN_ffff0000907389b8

void FUN_ffff0000907389b8(void)

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
  
  iVar1 = FUN_ffff00009078a934();
  uVar4 = (ulong)(iVar1 + 0xfffU & 0xfffff000) + 0xffff & 0x1ffff0000;
  iVar1 = FUN_ffff00009071c57c();
  if (iVar1 == 0) {
    lVar2 = FUN_ffff000090719798
                      (uVar4,0x1000000,0x90000000,0,0,s_reserve_memory_scp_share_ffff0000907e61c8);
  }
  else {
    lVar2 = FUN_ffff000090719f58
                      (uVar4,0x1000000,0x250000000,0x2a0000000,0,0,
                       s_reserve_memory_scp_share_ffff0000907e61c8);
  }
  if (lVar2 == 0) {
    FUN_ffff00009077de54
              (s__s__d___SCP__mblock_reserve_fail_ffff0000907e49a2,
               s_scp_sharedram_alloc_ffff000090802586,0x281);
  }
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  plVar3 = (long *)FUN_ffff00009071a204(s_scp_aov_reserved_ffff0000907e799e,0);
  lVar5 = *plVar3;
  if (lVar5 == 0) {
    FUN_ffff00009077de54
              (s__s__d___SCP__aov_mblock_reserve_f_ffff0000907d925d,
               s_scp_sharedram_alloc_ffff000090802586,0x28b);
  }
  FUN_ffff00009071ea6c(0xc200040f,0,lVar2,uVar4,0,0,0,0,&local_60);
  FUN_ffff00009071ea6c(0xc200040f,6,lVar5,0x1000000,0,0,0,0,&local_60);
  lStack_78 = lVar2 + uVar4;
  uStack_68 = 0;
  local_70 = 0x1b;
  local_80 = lVar2;
  FUN_ffff00009078ae3c(&local_80);
  uStack_68 = 0;
  local_70 = 0x3c;
  local_80 = lVar5;
  lStack_78 = lVar5 + 0xe00000;
  FUN_ffff00009078ae3c(&local_80);
  lStack_78 = lVar5 + 0x1000000;
  uStack_68 = 0;
  local_70 = 0x3d;
  local_80 = lVar5 + 0xe00000;
  FUN_ffff00009078ae3c(&local_80);
  return;
}


