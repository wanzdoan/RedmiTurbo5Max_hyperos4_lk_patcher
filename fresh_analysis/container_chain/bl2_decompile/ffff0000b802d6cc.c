
void FUN_ffff0000b802d6cc(int param_1,int param_2,uint param_3,uint param_4)

{
  long lVar1;
  uint uVar2;
  undefined4 uVar3;
  undefined4 uVar4;
  
  if (param_1 == 0x1e) {
    lVar1 = 0x10000;
  }
  else if ((param_1 == 0xb9) || (param_1 != 0xb8)) {
    lVar1 = 0;
  }
  else {
    lVar1 = 0x420000;
  }
  *(undefined4 *)(lVar1 + -0xffffcdeefeec) = 0;
  *(undefined4 *)(lVar1 + -0xffffcdeefee8) = 0x3004;
  *(undefined4 *)(lVar1 + -0xffffcdeefee4) = 0;
  *(undefined4 *)(lVar1 + -0xffffcdeefee0) = 0;
  *(undefined4 *)(lVar1 + -0xffffcdeefedc) = 0;
  *(undefined4 *)(lVar1 + -0xffffcdeefed8) = 0;
  *(undefined4 *)(lVar1 + -0xffffcdeefed4) = 0;
  *(undefined4 *)(lVar1 + -0xffffcdeefed0) = 0x11;
  *(undefined4 *)(lVar1 + -0xffffcdeefecc) = 0;
  *(undefined4 *)(lVar1 + -0xffffcdeefec8) = 0;
  if (param_4 == 0x18) {
    uVar4 = 0x20202020;
    uVar3 = 0x20200001;
  }
  else if (param_4 == 0x12) {
    uVar3 = 0x40400001;
    uVar4 = 0x40404040;
  }
  else {
    if (param_4 != 0x10) {
      if (param_4 < 0x19) {
        uVar2 = 0;
        *(undefined4 *)(&DAT_ffff000032110100 + lVar1) = 0;
      }
      else {
        uVar2 = 1;
      }
      goto LAB_ffff0000b802d7d8;
    }
    uVar3 = 0x50500001;
    uVar4 = 0x50504040;
  }
  *(undefined4 *)(lVar1 + -0xffffcdeefec4) = uVar3;
  *(undefined4 *)(lVar1 + -0xffffcdeefec0) = uVar4;
  uVar2 = 1;
  *(undefined4 *)(&DAT_ffff000032110100 + lVar1) = 1;
LAB_ffff0000b802d7d8:
  *(uint *)(lVar1 + -0xffffcdef0000) = *(uint *)(lVar1 + -0xffffcdef0000) & 0xfffffffe | uVar2;
  *(uint *)(&DAT_ffff000032110020 + lVar1) = *(uint *)(&DAT_ffff000032110020 + lVar1) | 3;
  *(uint *)(lVar1 + -0xffffcdeeffd0) = param_3 | param_2 << 0x10;
  return;
}


