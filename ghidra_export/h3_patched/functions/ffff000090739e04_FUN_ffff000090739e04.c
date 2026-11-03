// ffff000090739e04  size=468  FUN_ffff000090739e04

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090739e04(long param_1)

{
  long lVar1;
  long lVar2;
  undefined4 uVar3;
  undefined4 uVar4;
  undefined4 uVar5;
  undefined4 uVar6;
  uint uVar7;
  long lVar8;
  ulong uVar9;
  
  _DAT_ffff00009086de3c = *(uint *)(param_1 + 8);
  _DAT_ffff00009086de40 = *(uint *)(param_1 + 0xc);
  if (2 < _DAT_ffff00009086de3c) {
    FUN_ffff00009077de54(s_DBG_ERR_FLAG_MAX__d__<_dbg_err_f_ffff0000907e13c1,2);
    return;
  }
  if (_DAT_ffff00009086de3c != 0) {
    lVar8 = 0;
    uVar9 = 0;
    do {
      lVar1 = param_1 + lVar8;
      *(undefined8 *)(lVar8 + -0xffff6f7921bc) = *(undefined8 *)(lVar1 + 0x10);
      uVar4 = *(undefined4 *)(lVar1 + 0x1c);
      *(undefined4 *)(lVar8 + -0xffff6f7921b4) = *(undefined4 *)(lVar1 + 0x18);
      *(undefined4 *)(lVar8 + -0xffff6f7921b0) = uVar4;
      *(undefined8 *)(lVar8 + -0xffff6f792198) = *(undefined8 *)(lVar1 + 0x34);
      *(undefined4 *)(lVar8 + -0xffff6f792190) = *(undefined4 *)(lVar1 + 0x3c);
      FUN_ffff00009077e858(lVar8 + -0xffff6f792188,lVar1 + 0x44,0x40);
      uVar7 = _DAT_ffff00009086de40;
      if ((int)uVar9 == 0) {
        lVar2 = param_1 + lVar8;
        if (_DAT_ffff00009086de40 < 3) {
          uVar4 = *(undefined4 *)(lVar2 + 0x24);
          uVar3 = *(undefined4 *)(lVar2 + 0x30);
          *(undefined4 *)(&DAT_ffff00009086de54 + lVar8) = *(undefined4 *)(lVar1 + 0x20);
          *(undefined4 *)(&DAT_ffff00009086de58 + lVar8) = uVar4;
          *(undefined4 *)(&DAT_ffff00009086de64 + lVar8) = uVar3;
        }
        else {
          _DAT_ffff00009086dfe0 = *(undefined4 *)(lVar2 + 0x24);
          _DAT_ffff00009086dfe4 = *(undefined4 *)(lVar2 + 0x30);
          _DAT_ffff00009086dfdc = *(undefined4 *)(lVar1 + 0x20);
        }
        lVar1 = param_1 + lVar8;
        uVar4 = *(undefined4 *)(lVar1 + 0x2c);
        *(undefined4 *)(&DAT_ffff00009086de5c + lVar8) = *(undefined4 *)(lVar1 + 0x28);
        *(undefined4 *)(&DAT_ffff00009086de60 + lVar8) = uVar4;
        if (1 < uVar7) {
          *(undefined4 *)(lVar8 + -0xffff6f79218c) = *(undefined4 *)(lVar1 + 0x40);
        }
      }
      else if ((int)uVar9 == 1) {
        lVar1 = param_1 + lVar8;
        *(undefined8 *)(lVar8 + -0xffff6f792148) = *(undefined8 *)(lVar1 + 0x84);
        *(undefined4 *)(&DAT_ffff00009086dec0 + lVar8) = *(undefined4 *)(lVar1 + 0x8c);
        *(undefined8 *)(&DAT_ffff00009086dec4 + lVar8) = *(undefined8 *)(lVar1 + 0x90);
        *(undefined4 *)(&DAT_ffff00009086decc + lVar8) = *(undefined4 *)(lVar1 + 0x98);
        *(undefined8 *)(&DAT_ffff00009086ded0 + lVar8) = *(undefined8 *)(lVar1 + 0x9c);
        uVar4 = *(undefined4 *)(lVar1 + 0xac);
        uVar5 = *(undefined4 *)(lVar1 + 0xb0);
        *(undefined4 *)(lVar8 + -0xffff6f792128) = *(undefined4 *)(lVar1 + 0xa4);
        uVar3 = *(undefined4 *)(lVar1 + 0xb4);
        uVar6 = *(undefined4 *)(lVar1 + 0xb8);
        *(undefined4 *)(lVar8 + -0xffff6f792120) = uVar4;
        *(undefined4 *)(lVar8 + -0xffff6f79211c) = uVar5;
        uVar4 = *(undefined4 *)(lVar1 + 0xbc);
        uVar5 = *(undefined4 *)(lVar1 + 0xc0);
        *(undefined4 *)(&DAT_ffff00009086dee8 + lVar8) = uVar3;
        *(undefined4 *)(&DAT_ffff00009086deec + lVar8) = uVar6;
        uVar3 = *(undefined4 *)(lVar1 + 0xc4);
        uVar6 = *(undefined4 *)(lVar1 + 200);
        *(undefined4 *)(&DAT_ffff00009086def0 + lVar8) = uVar4;
        *(undefined4 *)(&DAT_ffff00009086def4 + lVar8) = uVar5;
        uVar4 = *(undefined4 *)(lVar1 + 0xcc);
        uVar5 = *(undefined4 *)(lVar1 + 0xd0);
        *(undefined4 *)(&DAT_ffff00009086def8 + lVar8) = uVar3;
        *(undefined4 *)(&DAT_ffff00009086defc + lVar8) = uVar6;
        uVar3 = *(undefined4 *)(lVar1 + 0xd4);
        uVar6 = *(undefined4 *)(lVar1 + 0xd8);
        *(undefined4 *)(lVar8 + -0xffff6f792100) = uVar4;
        *(undefined4 *)(lVar8 + -0xffff6f7920fc) = uVar5;
        *(undefined4 *)(&DAT_ffff00009086df08 + lVar8) = uVar3;
        *(undefined4 *)(&DAT_ffff00009086df0c + lVar8) = uVar6;
      }
      uVar9 = uVar9 + 1;
      lVar8 = lVar8 + 0xcc;
    } while (uVar9 < _DAT_ffff00009086de3c);
  }
  return;
}


