// File offset entry: 0x5c6e4

uint FUN_0205c5e4(undefined8 param_1,uint param_2)

{
  byte bVar1;
  byte *pbVar2;
  uint uVar3;
  uint uVar4;
  
  pbVar2 = (byte *)func_0x02006dbc();
  if (pbVar2 != (byte *)0x0) {
    bVar1 = *pbVar2;
    if ((bVar1 == 0x30) && (pbVar2[1] == 0x78)) {
      uVar4 = 0;
      pbVar2 = pbVar2 + 2;
      while( true ) {
        bVar1 = *pbVar2;
        if ((bVar1 == 0) ||
           (((byte)(bVar1 - 0x3a) < 0xf6 && ((byte)((bVar1 & 0xdf) + 0xb9) < 0xfa)))) break;
        pbVar2 = pbVar2 + 1;
        uVar3 = func_0x020911b0();
        uVar4 = uVar3 | uVar4 << 4;
      }
    }
    else {
      uVar4 = 0;
      while( true ) {
        uVar3 = (uint)bVar1;
        pbVar2 = pbVar2 + 1;
        if (uVar3 - 0x3a < 0xfffffff6) break;
        bVar1 = *pbVar2;
        uVar4 = (uVar3 + uVar4 * 10) - 0x30;
      }
    }
    return uVar4;
  }
  return param_2;
}


