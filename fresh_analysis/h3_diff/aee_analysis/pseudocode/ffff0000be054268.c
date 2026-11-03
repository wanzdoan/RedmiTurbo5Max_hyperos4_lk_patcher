
undefined4 FUN_ffff0000be054268(uint *param_1,int param_2,ulong *param_3)

{
  ulong *puVar1;
  uint uVar2;
  ulong uVar3;
  ulong uVar4;
  undefined4 uVar5;
  ulong uVar6;
  ulong uVar7;
  long lVar8;
  ulong uVar9;
  ulong uVar10;
  ulong uVar11;
  ulong uVar12;
  
  uVar5 = 0xffffffff;
  if ((param_1 != (uint *)0x0) && (param_3 != (ulong *)0x0)) {
    if ((param_2 == 0x80) || ((param_2 == 0x100 || (param_2 == 0xc0)))) {
      if (param_2 == 0x80) {
        uVar5 = 10;
      }
      else if (param_2 == 0xc0) {
        uVar5 = 0xc;
      }
      else {
        uVar5 = 0xe;
      }
      *(undefined4 *)(param_3 + 0x3c) = uVar5;
      uVar2 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
      uVar6 = (ulong)(uVar2 >> 0x10 | uVar2 << 0x10);
      *param_3 = uVar6;
      uVar2 = (param_1[1] & 0xff00ff00) >> 8 | (param_1[1] & 0xff00ff) << 8;
      uVar7 = (ulong)(uVar2 >> 0x10 | uVar2 << 0x10);
      param_3[1] = uVar7;
      uVar2 = (param_1[2] & 0xff00ff00) >> 8 | (param_1[2] & 0xff00ff) << 8;
      uVar9 = (ulong)(uVar2 >> 0x10 | uVar2 << 0x10);
      param_3[2] = uVar9;
      uVar2 = (param_1[3] & 0xff00ff00) >> 8 | (param_1[3] & 0xff00ff) << 8;
      uVar10 = (ulong)(uVar2 >> 0x10 | uVar2 << 0x10);
      param_3[3] = uVar10;
      if (param_2 == 0x80) {
        lVar8 = 0;
        do {
          uVar7 = param_3[3];
          puVar1 = (ulong *)((long)&DAT_ffff0000be0cd0d8 + lVar8);
          lVar8 = lVar8 + 8;
          uVar6 = uVar6 ^ (ulong)(byte)(&DAT_ffff0000be0cc8db)[(uVar7 >> 0x10 & 0xff) * 8] << 0x18 ^
                  (ulong)(byte)(&DAT_ffff0000be0cc8da)[(uVar7 >> 8 & 0xff) * 8] << 0x10 ^
                  (ulong)(byte)(&DAT_ffff0000be0cc8d9)[(uVar7 & 0xff) * 8] << 8 ^
                  (ulong)(byte)(&DAT_ffff0000be0cc8d8)[uVar7 >> 0x15 & 0x7fffffffff8] ^ *puVar1;
          uVar9 = param_3[2] ^ param_3[1] ^ uVar6;
          param_3[4] = uVar6;
          param_3[5] = param_3[1] ^ uVar6;
          param_3[6] = uVar9;
          param_3[7] = uVar9 ^ uVar7;
          param_3 = param_3 + 4;
        } while (lVar8 != 0x50);
      }
      else {
        uVar2 = (param_1[4] & 0xff00ff00) >> 8 | (param_1[4] & 0xff00ff) << 8;
        param_3[4] = (ulong)(uVar2 >> 0x10 | uVar2 << 0x10);
        uVar2 = (param_1[5] & 0xff00ff00) >> 8 | (param_1[5] & 0xff00ff) << 8;
        uVar11 = (ulong)(uVar2 >> 0x10 | uVar2 << 0x10);
        param_3[5] = uVar11;
        if (param_2 == 0xc0) {
          lVar8 = 0;
          param_3 = param_3 + 0xb;
          while( true ) {
            uVar6 = uVar6 ^ (ulong)(byte)(&DAT_ffff0000be0cc8db)[(uVar11 >> 0x10 & 0xff) * 8] <<
                            0x18 ^
                    (ulong)(byte)(&DAT_ffff0000be0cc8da)[(uVar11 >> 8 & 0xff) * 8] << 0x10 ^
                    (ulong)(byte)(&DAT_ffff0000be0cc8d9)[(uVar11 & 0xff) * 8] << 8 ^
                    (ulong)(byte)(&DAT_ffff0000be0cc8d8)[uVar11 >> 0x15 & 0x7fffffffff8] ^
                    *(ulong *)((long)&DAT_ffff0000be0cd0d8 + lVar8);
            uVar7 = uVar7 ^ uVar6;
            uVar9 = uVar9 ^ uVar7;
            uVar10 = uVar10 ^ uVar9;
            param_3[-5] = uVar6;
            param_3[-4] = uVar7;
            param_3[-3] = uVar9;
            param_3[-2] = uVar10;
            if (lVar8 == 0x38) break;
            lVar8 = lVar8 + 8;
            uVar11 = param_3[-7] ^ uVar10 ^ uVar11;
            param_3[-1] = param_3[-7] ^ uVar10;
            *param_3 = uVar11;
            param_3 = param_3 + 6;
          }
        }
        else {
          uVar2 = (param_1[6] & 0xff00ff00) >> 8 | (param_1[6] & 0xff00ff) << 8;
          param_3[6] = (ulong)(uVar2 >> 0x10 | uVar2 << 0x10);
          uVar2 = (param_1[7] & 0xff00ff00) >> 8 | (param_1[7] & 0xff00ff) << 8;
          uVar11 = (ulong)(uVar2 >> 0x10 | uVar2 << 0x10);
          param_3[7] = uVar11;
          if (param_2 == 0x100) {
            lVar8 = 0;
            while( true ) {
              uVar6 = uVar6 ^ (ulong)(byte)(&DAT_ffff0000be0cc8db)[(uVar11 >> 0x10 & 0xff) * 8] <<
                              0x18 ^
                      (ulong)(byte)(&DAT_ffff0000be0cc8da)[(uVar11 >> 8 & 0xff) * 8] << 0x10 ^
                      (ulong)(byte)(&DAT_ffff0000be0cc8d9)[(uVar11 & 0xff) * 8] << 8 ^
                      (ulong)(byte)(&DAT_ffff0000be0cc8d8)[uVar11 >> 0x15 & 0x7fffffffff8] ^
                      *(ulong *)((long)&DAT_ffff0000be0cd0d8 + lVar8);
              uVar7 = uVar7 ^ uVar6;
              uVar9 = uVar9 ^ uVar7;
              uVar10 = uVar10 ^ uVar9;
              param_3[8] = uVar6;
              param_3[9] = uVar7;
              param_3[10] = uVar9;
              param_3[0xb] = uVar10;
              if (lVar8 == 0x30) break;
              lVar8 = lVar8 + 8;
              uVar12 = param_3[4] ^
                       (ulong)(byte)(&DAT_ffff0000be0cc8db)[(uVar10 >> 0x18) * 8] << 0x18 ^
                       (ulong)(byte)(&DAT_ffff0000be0cc8da)[(uVar10 >> 0x10 & 0xff) * 8] << 0x10 ^
                       (ulong)(byte)(&DAT_ffff0000be0cc8d9)[(uVar10 >> 8 & 0xff) * 8] << 8 ^
                       (ulong)(byte)(&DAT_ffff0000be0cc8d8)[(uVar10 & 0xff) * 8];
              uVar3 = param_3[5] ^ uVar12;
              uVar4 = param_3[6] ^ uVar3;
              uVar11 = uVar4 ^ uVar11;
              param_3[0xc] = uVar12;
              param_3[0xd] = uVar3;
              param_3[0xe] = uVar4;
              param_3[0xf] = uVar11;
              param_3 = param_3 + 8;
            }
          }
        }
      }
      uVar5 = 0;
    }
    else {
      uVar5 = 0xfffffffe;
    }
  }
  return uVar5;
}


