// ffff0000907c0070  size=376  FUN_ffff0000907c0070

void FUN_ffff0000907c0070(long param_1,int param_2,undefined8 param_3)

{
  long lVar1;
  undefined8 uVar2;
  undefined8 uVar3;
  long lVar4;
  ulong uVar5;
  undefined8 local_1d0;
  undefined8 uStack_1c8;
  undefined8 local_1c0;
  undefined8 uStack_1b8;
  undefined8 local_1b0;
  undefined8 uStack_1a8;
  undefined8 local_1a0;
  undefined8 uStack_198;
  undefined8 local_190;
  undefined8 uStack_188;
  undefined8 local_180;
  undefined8 uStack_178;
  undefined8 local_170;
  undefined8 uStack_168;
  undefined8 local_160;
  undefined8 uStack_158;
  undefined8 local_150;
  undefined8 uStack_148;
  undefined8 local_140;
  undefined8 uStack_138;
  undefined8 local_130;
  undefined8 uStack_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 uStack_108;
  
  if (param_2 == 1) {
    FUN_ffff00009077ee50(&local_1d0,0,400);
    FUN_ffff0000907be2e4(&local_1d0);
    if (*(long *)(param_1 + 0x10) != 0) {
      lVar4 = 0;
      uVar5 = 0;
      do {
        lVar1 = *(long *)(param_1 + 8) + lVar4;
        FUN_ffff0000907be384(&local_1d0,*(undefined8 *)(lVar1 + 8),*(undefined8 *)(lVar1 + 0x10));
        uVar5 = uVar5 + 1;
        lVar4 = lVar4 + 0x20;
      } while (uVar5 < *(ulong *)(param_1 + 0x10));
    }
    uVar2 = FUN_ffff0000907be638(&local_1d0);
    uVar3 = 0x40;
  }
  else {
    if (param_2 != 0) {
      uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
      FUN_ffff0000907c0ee8
                (uVar2,&DAT_ffff0000907ebfa6,&DAT_ffff0000907e59fe,s___FATAL__ffff0000907f5b19,
                 s_Unknown_digest_type_ffff0000907d3c66,0);
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000907c0ee4();
    }
    local_110 = 0;
    uStack_108 = 0;
    local_120 = 0;
    uStack_118 = 0;
    local_130 = 0;
    uStack_128 = 0;
    local_140 = 0;
    uStack_138 = 0;
    local_150 = 0;
    uStack_148 = 0;
    local_160 = 0;
    uStack_158 = 0;
    local_170 = 0;
    uStack_168 = 0;
    local_180 = 0;
    uStack_178 = 0;
    local_190 = 0;
    uStack_188 = 0;
    local_1a0 = 0;
    uStack_198 = 0;
    local_1b0 = 0;
    uStack_1a8 = 0;
    local_1c0 = 0;
    uStack_1b8 = 0;
    local_1d0 = 0;
    uStack_1c8 = 0;
    FUN_ffff0000907c6bd8(&local_1d0);
    if (*(long *)(param_1 + 0x10) != 0) {
      lVar4 = 0;
      uVar5 = 0;
      do {
        lVar1 = *(long *)(param_1 + 8) + lVar4;
        FUN_ffff0000907c6c10(&local_1d0,*(undefined8 *)(lVar1 + 8),*(undefined8 *)(lVar1 + 0x10));
        uVar5 = uVar5 + 1;
        lVar4 = lVar4 + 0x20;
      } while (uVar5 < *(ulong *)(param_1 + 0x10));
    }
    uVar2 = FUN_ffff0000907c6c54(&local_1d0);
    uVar3 = 0x20;
  }
  thunk_FUN_ffff00009077eb20(param_3,uVar2,uVar3);
  return;
}


