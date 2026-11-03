
undefined8
FUN_ffff0000be0091e4
          (byte *param_1,byte *param_2,int param_3,long param_4,char *param_5,long param_6,
          uint *param_7)

{
  ulong uVar1;
  uint uVar2;
  uint uVar3;
  int iVar4;
  uint uVar5;
  long lVar6;
  
  uVar5 = *param_7;
  if (param_3 != 0) {
    lVar6 = (long)param_3;
    do {
      if (uVar5 == 0) {
        if ((param_4 == 0 || param_5 == (char *)0x0) || param_6 == 0) {
          FUN_ffff0000be05fd98
                    (s__s__d__aes_or_buf_is_null_ffff0000be0a9ecc,
                     s_mrdump_aes_encrypt_ffff0000be0bdf50,0x5b);
          FUN_ffff0000be05fd98
                    (s__s__d___s__d__encrypt_error_ffff0000be0c3b1d,
                     s_mrdump_AES_ctr128_encrypt_ffff0000be0a41cd,0x83,
                     s_mrdump_AES_ctr128_encrypt_ffff0000be0a41cd,0x83);
          return 0xffffffff;
        }
        FUN_ffff0000be054544(param_5,param_6,param_4);
        uVar2 = (uint)(byte)param_5[0xe] + ((byte)param_5[0xf] + 1 >> 8);
        param_5[0xf] = (char)((byte)param_5[0xf] + 1);
        uVar3 = (uint)(byte)param_5[0xd] + (uVar2 >> 8);
        param_5[0xe] = (char)uVar2;
        uVar2 = (uint)(byte)param_5[0xc] + (uVar3 >> 8);
        param_5[0xd] = (char)uVar3;
        uVar3 = (uint)(byte)param_5[0xb] + (uVar2 >> 8);
        param_5[0xc] = (char)uVar2;
        uVar2 = (uint)(byte)param_5[10] + (uVar3 >> 8);
        param_5[0xb] = (char)uVar3;
        uVar3 = (uint)(byte)param_5[9] + (uVar2 >> 8);
        param_5[10] = (char)uVar2;
        uVar2 = (uint)(byte)param_5[8] + (uVar3 >> 8);
        param_5[9] = (char)uVar3;
        uVar3 = (uint)(byte)param_5[7] + (uVar2 >> 8);
        param_5[8] = (char)uVar2;
        uVar2 = (uint)(byte)param_5[6] + (uVar3 >> 8);
        param_5[7] = (char)uVar3;
        uVar3 = (uint)(byte)param_5[5] + (uVar2 >> 8);
        param_5[6] = (char)uVar2;
        uVar2 = (uint)(byte)param_5[4] + (uVar3 >> 8);
        param_5[5] = (char)uVar3;
        uVar3 = (uint)(byte)param_5[3] + (uVar2 >> 8);
        param_5[4] = (char)uVar2;
        uVar2 = (uint)(byte)param_5[2] + (uVar3 >> 8);
        param_5[3] = (char)uVar3;
        iVar4 = (uint)(byte)param_5[1] + (uVar2 >> 8);
        param_5[2] = (char)uVar2;
        param_5[1] = (char)iVar4;
        *param_5 = *param_5 + (char)((uint)iVar4 >> 8);
      }
      uVar1 = (ulong)uVar5;
      lVar6 = lVar6 + -1;
      uVar5 = uVar5 + 1 & 0xf;
      *param_2 = *(byte *)(param_6 + uVar1) ^ *param_1;
      param_2 = param_2 + 1;
      param_1 = param_1 + 1;
    } while (lVar6 != 0);
  }
  *param_7 = uVar5;
  return 0;
}


