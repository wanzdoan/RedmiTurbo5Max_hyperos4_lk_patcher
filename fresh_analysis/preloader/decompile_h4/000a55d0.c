// File offset entry: 0xa55d0

undefined8 FUN_020a54d0(uint param_1)

{
  int iVar1;
  undefined4 uVar2;
  undefined8 *puVar3;
  undefined4 *puVar4;
  undefined8 uVar5;
  undefined8 uVar6;
  
  *puRam0000000002103810 = param_1;
  if (param_1 < 3) {
    iVar1 = param_1 * 0x80 + 0x100;
    uVar2 = *(undefined4 *)((long)(int)param_1 * 4 + 0x20e41b8);
    uVar5 = 0;
    uVar6 = *(undefined8 *)((long)(int)param_1 * 8 + 0x2100ee8);
    *piRam0000000002103838 = iVar1;
    puVar4 = puRam0000000002103828;
    puVar3 = puRam0000000002103820;
    *piRam0000000002103818 = iVar1;
    *puVar4 = uVar2;
    *puVar3 = uVar6;
  }
  else {
    uVar5 = 0x10101b;
  }
  return uVar5;
}


