// ffff00009074b814  size=312  FUN_ffff00009074b814

void FUN_ffff00009074b814(long param_1,undefined8 param_2,int param_3,uint param_4,uint param_5)

{
  uint uVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  
  *(undefined4 *)(param_1 + -0xffffcdeefeec) = 0;
  *(undefined4 *)(param_1 + -0xffffcdeefee8) = 0x3004;
  *(undefined4 *)(param_1 + -0xffffcdeefee4) = 0;
  *(undefined4 *)(param_1 + -0xffffcdeefee0) = 0;
  *(undefined4 *)(param_1 + -0xffffcdeefedc) = 0;
  *(undefined4 *)(param_1 + -0xffffcdeefed8) = 0;
  *(undefined4 *)(param_1 + -0xffffcdeefed4) = 0;
  *(undefined4 *)(param_1 + -0xffffcdeefed0) = 0x11;
  *(undefined4 *)(param_1 + -0xffffcdeefecc) = 0;
  *(undefined4 *)(param_1 + -0xffffcdeefec8) = 0;
  if (param_5 == 0x18) {
    uVar3 = 0x20202020;
    uVar2 = 0x20200001;
  }
  else if (param_5 == 0x12) {
    uVar2 = 0x40400001;
    uVar3 = 0x40404040;
  }
  else {
    if (param_5 != 0x10) {
      if (param_5 < 0x19) {
        uVar1 = 0;
        *(undefined4 *)(&DAT_ffff000032110100 + param_1) = 0;
      }
      else {
        uVar1 = 1;
      }
      goto LAB_ffff00009074b8f0;
    }
    uVar2 = 0x50500001;
    uVar3 = 0x50504040;
  }
  *(undefined4 *)(param_1 + -0xffffcdeefec4) = uVar2;
  *(undefined4 *)(param_1 + -0xffffcdeefec0) = uVar3;
  uVar1 = 1;
  *(undefined4 *)(&DAT_ffff000032110100 + param_1) = 1;
LAB_ffff00009074b8f0:
  *(uint *)(param_1 + -0xffffcdef0000) = *(uint *)(param_1 + -0xffffcdef0000) & 0xfffffffe | uVar1;
  *(uint *)(&DAT_ffff000032110020 + param_1) = *(uint *)(&DAT_ffff000032110020 + param_1) | 3;
  *(uint *)(param_1 + -0xffffcdeeffd0) = param_4 | param_3 << 0x10;
  return;
}


