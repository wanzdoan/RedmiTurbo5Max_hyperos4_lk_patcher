// ffff000090745b60  size=308  FUN_ffff000090745b60

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090745b60(undefined8 param_1)

{
  int iVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  undefined8 *puVar4;
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
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  
  if (_DAT_ffff000090877590 != 0) {
    local_150 = 0;
    uStack_148 = 0;
    local_140 = 0;
    uStack_138 = 0;
    local_130 = 0;
    uStack_128 = 0;
    local_120 = 0;
    uStack_118 = 0;
    local_110 = 0;
    uStack_108 = 0;
    local_100 = 0;
    uStack_f8 = 0;
    local_f0 = 0;
    uStack_e8 = 0;
    local_e0 = 0;
    uStack_d8 = 0;
    local_d0 = 0;
    uStack_c8 = 0;
    local_c0 = 0;
    uStack_b8 = 0;
    local_b0 = 0;
    uStack_a8 = 0;
    local_a0 = 0;
    uStack_98 = 0;
    local_90 = 0;
    uStack_88 = 0;
    local_80 = 0;
    uStack_78 = 0;
    local_70 = 0;
    uStack_68 = 0;
    local_60 = 0;
    uStack_58 = 0;
    iVar1 = FUN_ffff00009077f6b8(&local_150,param_1,&stack0xfffffffffffffe90);
    if (iVar1 < 0) {
      FUN_ffff00009077e11c
                (s__s__d_vsprintf_failed_ffff000090800e86,s_video_printf_ffff0000907d7ca5,0x25b);
    }
    else {
      iVar1 = FUN_ffff00009077f250(&local_150);
      if (iVar1 != 0) {
        puVar4 = &local_150;
        do {
          iVar1 = iVar1 + -1;
          FUN_ffff000090744e30(*(undefined1 *)puVar4);
          puVar4 = (undefined8 *)((long)puVar4 + 1);
        } while (iVar1 != 0);
      }
      if (_DAT_ffff000090877590 == 0) {
        uVar2 = 0;
        uVar3 = 0;
      }
      else {
        uVar2 = *(undefined4 *)(_DAT_ffff000090877590 + 0x14);
        uVar3 = *(undefined4 *)(_DAT_ffff000090877590 + 0x18);
      }
      FUN_ffff00009075c7b0(0,0,uVar2,uVar3);
    }
  }
  return;
}


