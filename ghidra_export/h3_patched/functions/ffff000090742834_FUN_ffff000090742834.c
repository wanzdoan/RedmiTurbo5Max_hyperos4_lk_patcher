// ffff000090742834  size=264  FUN_ffff000090742834

void FUN_ffff000090742834(int param_1,uint param_2)

{
  long lVar1;
  uint uVar2;
  uint *puVar3;
  uint uVar4;
  uint uVar5;
  
  puVar3 = *(uint **)(s_0123456789ABCDEF_ffff000090804b08 + (long)param_1 * 8 + 0x10);
  puVar3[2] = 7;
  puVar3[3] = 3;
  if (param_2 < 0x1c201) {
    uVar4 = 0;
    if (param_2 << 4 != 0) {
      uVar4 = (param_2 * 8 + 26000000) / (param_2 << 4);
    }
    uVar5 = 0;
  }
  else {
    uVar5 = param_2 * 0x100;
    uVar4 = 0;
    if (uVar5 != 0) {
      uVar4 = (uVar5 + 25999999) / uVar5;
    }
    uVar5 = 0;
    if (param_2 != 0) {
      uVar5 = 2600000000 / param_2;
    }
    uVar2 = 0;
    if (uVar4 != 0) {
      uVar2 = uVar5 / uVar4;
    }
    uVar5 = 0;
    if (uVar4 * param_2 != 0) {
      uVar5 = 26000000 / (uVar4 * param_2);
    }
    puVar3[10] = uVar5 - 1;
    lVar1 = (ulong)((uVar2 + ((uVar2 >> 2 & 0x3fff) / 0x19) * -100 + 5 & 0xff) / 10) * 2;
    puVar3[0xb] = (uVar5 - 1 >> 1) - 1;
    puVar3[0x15] = (uint)*(ushort *)(&DAT_ffff000090804b48 + lVar1);
    uVar5 = 3;
    puVar3[0x16] = (uint)*(ushort *)(&DAT_ffff000090804b5e + lVar1);
  }
  puVar3[9] = uVar5;
  uVar5 = puVar3[3];
  puVar3[3] = uVar5 | 0x80;
  *puVar3 = uVar4 & 0xff;
  puVar3[1] = uVar4 >> 8 & 0xff;
  puVar3[3] = uVar5;
  return;
}


