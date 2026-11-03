// ffff000090743c78  size=580  FUN_ffff000090743c78

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090743c78(long param_1)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  uint uVar5;
  ulong uVar6;
  int iVar7;
  undefined4 *puVar8;
  long lVar9;
  
  iVar7 = _DAT_ffff000090877488 - _DAT_ffff000090877480;
  FUN_ffff00009077de54
            (s__s__d___VCP__tbuf_size___0x_x_ffff000090800c51,
             s_vcp_do_tbufdump_RV55_ffff0000907dfba3,0xfc,iVar7);
  if (iVar7 - 0x4001U < 0xffffc000) {
    FUN_ffff00009077de54
              (s__VCP__error___s_tbuf_size_0x_x___ffff0000907e4c1d,
               s_vcp_do_tbufdump_RV55_ffff0000907dfba3,iVar7,0x4000);
    iVar7 = 0;
  }
  else {
    FUN_ffff00009077eb88(param_1,0,iVar7);
    uVar2 = _DAT_ffff000031a0a08c;
    lVar9 = 0;
    uVar3 = _DAT_ffff000031a0a08c & 0x1f;
    uVar5 = _DAT_ffff000031a0a08c & 0x1f;
    uVar1 = _DAT_ffff000031a0a010 & 0xfff0ff0f;
    do {
      _DAT_ffff000031a0a010 = uVar1 + (uVar3 >> 2 & 7) * 0x10010;
      uVar6 = (ulong)(uVar5 * 8 + (int)lVar9 & 0x18);
      uVar3 = uVar3 + 1;
      puVar8 = (undefined4 *)(param_1 + lVar9);
      lVar9 = lVar9 + 8;
      *puVar8 = *(undefined4 *)(uVar6 | 0xffff000031a0a0e0);
      puVar8[1] = *(undefined4 *)(uVar6 | 0xffff000031a0a0e4);
    } while ((int)lVar9 != 0x100);
    uVar6 = 0;
    puVar8 = (undefined4 *)(param_1 + 4);
    do {
      FUN_ffff00009077de54
                (s__s__d___VCP__C0_H0__02d_0x_08x___ffff0000907f9dbd,
                 s_vcp_do_tbufdump_RV55_ffff0000907dfba3,0x114,uVar6 & 0xffffffff,puVar8[-1],*puVar8
                );
      uVar6 = uVar6 + 1;
      puVar8 = puVar8 + 2;
    } while (uVar6 != 0x20);
    lVar4 = 0;
    uVar3 = uVar2 >> 8;
    do {
      uVar6 = (ulong)((uVar2 >> 5 & 0x7fffff8) + (int)lVar4 & 0x18);
      _DAT_ffff000031a0a010 = uVar1 + (uVar3 >> 2 & 7) * 0x10010 + 0x80080;
      uVar3 = uVar3 + 1;
      puVar8 = (undefined4 *)(param_1 + lVar4 + lVar9);
      lVar4 = lVar4 + 8;
      *puVar8 = *(undefined4 *)(uVar6 | 0xffff000031a0a0e0);
      puVar8[1] = *(undefined4 *)(uVar6 | 0xffff000031a0a0e4);
    } while ((int)lVar4 != 0x100);
    uVar6 = 0;
    puVar8 = (undefined4 *)(param_1 + 0x104);
    do {
      FUN_ffff00009077de54
                (s__s__d___VCP__C0_H1__02d_0x_08x___ffff0000907de31a,
                 s_vcp_do_tbufdump_RV55_ffff0000907dfba3,0x121,uVar6 & 0xffffffff,puVar8[-1],*puVar8
                );
      uVar6 = uVar6 + 1;
      puVar8 = puVar8 + 2;
    } while (uVar6 != 0x20);
  }
  return iVar7;
}


