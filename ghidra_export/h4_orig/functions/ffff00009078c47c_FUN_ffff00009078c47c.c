// ffff00009078c47c  size=2196  FUN_ffff00009078c47c

/* WARNING: Type propagation algorithm not settling */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009078c47c(void)

{
  long lVar1;
  undefined1 *puVar2;
  undefined *puVar3;
  byte bVar4;
  undefined8 *puVar5;
  bool bVar6;
  int iVar7;
  undefined2 *puVar8;
  char *pcVar9;
  undefined8 uVar10;
  uint uVar11;
  uint uVar12;
  long lVar13;
  uint *puVar14;
  uint uVar15;
  long lVar16;
  ulong uVar17;
  uint uVar18;
  ulong uVar19;
  long *plVar20;
  uint uVar21;
  undefined8 *puVar22;
  undefined8 local_68;
  
  uVar21 = _DAT_ffff000016701004 & _DAT_ffff000016701000;
  if (((uVar21 >> 5 & 1) != 0) &&
     (_DAT_ffff000016701854 = _DAT_ffff000016701850 & _DAT_ffff000016701854,
     (_DAT_ffff000016701854 & 1) != 0)) {
    lVar13 = ((ulong)_DAT_ffff000016701800 & 7) - 1;
    uVar11 = (uint)lVar13;
    if ((uVar11 < 5) && ((0x1dU >> (ulong)(uVar11 & 0x1f) & 1) != 0)) {
      uVar11 = *(uint *)(&DAT_ffff0000908124f4 + lVar13 * 4);
      FUN_ffff00009077e11c
                (s__USB___s___d__is_detected_ffff0000907f566f,
                 &DAT_ffff0000908124e0 + *(int *)(&DAT_ffff0000908124e0 + (ulong)uVar11 * 4),
                 (ulong)uVar11);
      puVar22 = _DAT_ffff00009088a1b8;
      _DAT_ffff00009088a0cc = uVar11;
      iVar7 = FUN_ffff00009077f654(_DAT_ffff00009088a1b8 + 6,&DAT_ffff0000907e3bca);
      if (iVar7 < 0) {
        FUN_ffff00009077e11c(s__USB__ep0_>name__copy_failed_ffff0000907ed407);
      }
      uVar11 = _DAT_ffff00009088a0cc;
      *(undefined1 *)((long)puVar22 + 0x42) = 0;
      *(undefined1 *)(puVar22 + 8) = 0;
      _DAT_ffff000016701100 = 0x200;
      if (uVar11 != 3) {
        _DAT_ffff000016701100 = 0x40;
      }
      *(uint *)((long)puVar22 + 0x3c) = _DAT_ffff000016701100;
      _DAT_ffff000016701088 = _DAT_ffff000016701088 | 1;
    }
    else {
      FUN_ffff00009077e11c
                (s__USB___s___d__is_detected_ffff0000907f566f,s_UNKNOWN_ffff0000907d61b6,0);
      _DAT_ffff00009088a0cc = 0;
      FUN_ffff00009078cd10();
    }
  }
  if ((uVar21 >> 4 & 1) != 0) {
    _DAT_ffff00001670341c = _DAT_ffff000016703418 & _DAT_ffff00001670341c;
    if ((_DAT_ffff00001670341c >> 2 & 1) != 0) {
      _DAT_ffff00009088a0c8 = 0;
      DAT_ffff00009088a21c = 0;
    }
    if ((_DAT_ffff00001670341c & 1) != 0) {
      FUN_ffff00009078cd10();
    }
  }
  if ((uVar21 >> 2 & 1) != 0) {
    uVar11 = _DAT_ffff00001670253c & _DAT_ffff000016702540;
    if ((uVar11 & 0x30) != 0) {
      _DAT_ffff00009088a0c8 = 0;
      DAT_ffff00009088a21c = 0;
    }
    _DAT_ffff000016702540 = uVar11;
    if ((uVar11 >> 0x10 & 1) != 0) {
      _DAT_ffff00001670241c = _DAT_ffff00001670241c & 0xfffffffe;
      FUN_ffff00009077e11c(s__USB__U3_pullup_D_s_ffff0000907fa9ac,&DAT_ffff0000907dd9b4);
      _DAT_ffff00009088a0c8 = 0;
      DAT_ffff00009088a21c = 0;
    }
    if ((uVar11 >> 0xf & 1) != 0) {
      _DAT_ffff00001670241c = _DAT_ffff00001670241c | 1;
      FUN_ffff00009077e11c(s__USB__U3_pullup_D_s_ffff0000907fa9ac,&DAT_ffff0000907eef2c);
    }
    if ((uVar11 >> 10 & 1) != 0) {
      FUN_ffff00009078cd10();
    }
  }
  puVar22 = _DAT_ffff00009088a1b8;
  uVar11 = _DAT_ffff000016701100;
  if (((uVar21 & 1) == 0) ||
     (_DAT_ffff000016701080 = _DAT_ffff000016701084 & _DAT_ffff000016701080,
     (_DAT_ffff000016701080 & 1) == 0)) goto switchD_ffff00009078c664_default;
  if ((_DAT_ffff000016701100 >> 0x16 & 1) != 0) {
    _DAT_ffff000016701100 = _DAT_ffff000016701100 & 0xfdbcffff | 0x400000;
    _DAT_ffff00009088a0c8 = 0;
code_r0xffff00009078c720:
    if ((_DAT_ffff000016701100 >> 0x11 & 1) == 0) goto switchD_ffff00009078c664_default;
    if (_DAT_ffff000016701108 != 8) {
      FUN_ffff00009077e11c(s__USB__SETUP_packet_len__d____8__ffff0000907f8f41);
      goto switchD_ffff00009078c664_default;
    }
    local_68 = 0;
    FUN_ffff00009078cf10(0,&local_68,8);
    puVar22 = _DAT_ffff00009088a0c0;
    if ((local_68 & 0x60) == 0) {
      if ((char)(byte)local_68 < '\0') {
        plVar20 = (long *)*_DAT_ffff00009088a1b8;
        uVar11 = (uint)(local_68._1_1_ >> 1) | (uint)local_68._1_1_ << 0x1f;
        puVar8 = (undefined2 *)*plVar20;
        _DAT_ffff00009088a0c8 = 2;
        if (1 < uVar11 - 4) {
          if (uVar11 == 3) {
            puVar22 = (undefined8 *)&DAT_ffff00009088a200;
            if (2 < _DAT_ffff00009088a0cc) {
              puVar22 = (undefined8 *)&DAT_ffff00009088a210;
            }
            do {
              puVar22 = (undefined8 *)*puVar22;
              if (puVar22 == (undefined8 *)0x0) goto LAB_ffff00009078c754;
            } while (*(ushort *)(puVar22 + 1) != local_68._2_2_);
            uVar11 = (uint)*(ushort *)((long)puVar22 + 10);
            if ((uint)local_68._6_2_ <= (uint)*(ushort *)((long)puVar22 + 10)) {
              uVar11 = (uint)local_68._6_2_;
            }
            FUN_ffff00009077eb20(puVar8,(undefined1 *)((long)puVar22 + 0xc),uVar11);
            uVar18 = _DAT_ffff00009088a0cc;
            *(uint *)(plVar20 + 1) = uVar11;
            puVar5 = _DAT_ffff00009088a0c0;
            if (((uVar18 == 1) && (*(short *)(puVar22 + 1) == 0x200)) &&
               (*(char *)((long)_DAT_ffff00009088a0c0 + 0x1b) != '\0')) {
              lVar13 = 0;
              uVar18 = 0;
              lVar16 = *plVar20;
              do {
                if (uVar11 <= (int)lVar13 + 0x17U) break;
                lVar1 = lVar16 + lVar13;
                uVar18 = uVar18 + 1;
                lVar13 = lVar13 + 7;
                *(undefined2 *)(lVar1 + 0x16) = 0x40;
              } while (uVar18 < *(byte *)((long)puVar5 + 0x1b));
            }
          }
          else {
            if (uVar11 != 0) {
              pcVar9 = s__USB__Unsupported_command_with_T_ffff0000907ffe88;
              goto LAB_ffff00009078caec;
            }
            *(undefined4 *)(plVar20 + 1) = 2;
            *puVar8 = 0;
            if (((byte)local_68 & 0x1f) == 3) {
              *(undefined4 *)(plVar20 + 1) = 0;
            }
            else if ((local_68 & 0x1f) == 0) {
              *(undefined1 *)puVar8 = 1;
            }
          }
          goto joined_r0xffff00009078cadc;
        }
      }
      else {
        if (local_68._1_1_ != 1) {
          if (local_68._1_1_ == 9) {
            DAT_ffff00009088a21d = DAT_ffff00009088a21d & 0xfe | local_68._2_2_ != 0;
            if (local_68._2_2_ == 1) {
              if (_DAT_ffff00009088a0cc - 1 < 3) {
                uVar11 = *(uint *)(&DAT_ffff000090812508 +
                                  (long)(int)(_DAT_ffff00009088a0cc - 1) * 4);
              }
              else {
                uVar11 = 0x200;
              }
              lVar13 = 0;
              do {
                bVar4 = (&DAT_ffff00009088a168)[lVar13];
                uVar19 = (ulong)bVar4;
                uVar18 = (uint)bVar4;
                if (uVar18 != 0) {
                  *(uint *)(lVar13 + -0xffff6f775e9c) = uVar11;
                  uVar12 = uVar11;
                  if (*(char *)(lVar13 + -0xffff6f775e96) != '\x02') {
                    FUN_ffff00009077e11c(s__USB__ep_type_is_wrong__should_b_ffff0000907ded94);
                    uVar12 = *(uint *)(lVar13 + -0xffff6f775e9c);
                  }
                  if (uVar12 < 0x201) {
LAB_ffff00009078cbfc:
                    uVar12 = 0x90000;
                  }
                  else {
                    if (0x400 < uVar12) {
                      FUN_ffff00009077e11c(s__USB__The_maxp__d_is_not_support_ffff0000907eb944);
                      goto LAB_ffff00009078cbfc;
                    }
                    uVar12 = 0xa0000;
                  }
                  if ((&DAT_ffff00009088a169)[lVar13] == '\0') {
                    uVar15 = (uint)(1L << (uVar19 & 0x3f));
                    uVar17 = (ulong)(uVar18 * 0x10 - 0x10);
                    _DAT_ffff000016701804 = (_DAT_ffff000016701804 | uVar15) & (uVar15 ^ 0xffffffff)
                    ;
                    bVar4 = *(byte *)(lVar13 + -0xffff6f775e95);
                    puVar14 = (uint *)(uVar17 - 0xffffe98fede8);
                    _DAT_ffff000016701708 =
                         _DAT_ffff000016701708 | (uint)(0x10000L << (uVar19 & 0x3f));
                    uVar18 = _DAT_ffff00009088a0d4 +
                             (uVar18 + uVar18 * bVar4) * _DAT_ffff00009088a0d8;
                    *(uint *)(uVar17 - 0xffffe98fedf0) =
                         *(uint *)(lVar13 + -0xffff6f775e9c) | 0x20000000;
                    *(uint *)(uVar17 - 0xffffe98fedec) = (bVar4 & 0x3f) << 8 | 0x200;
                  }
                  else {
                    uVar15 = (uint)(0x10000L << (uVar19 & 0x3f));
                    uVar17 = (ulong)((uint)bVar4 * 0x10 - 0x10);
                    _DAT_ffff000016701804 = (_DAT_ffff000016701804 | uVar15) & (uVar15 ^ 0xffffffff)
                    ;
                    bVar4 = *(byte *)(lVar13 + -0xffff6f775e95);
                    puVar14 = (uint *)(uVar17 - 0xffffe98feee8);
                    _DAT_ffff000016701708 = _DAT_ffff000016701708 | (uint)(1L << (uVar19 & 0x3f));
                    uVar18 = _DAT_ffff00009088a0d0 +
                             (uVar18 + uVar18 * bVar4) * _DAT_ffff00009088a0d8;
                    *(uint *)(uVar17 - 0xffffe98feef0) =
                         *(uint *)(lVar13 + -0xffff6f775e9c) | 0x20000000;
                    *(uint *)(uVar17 - 0xffffe98feeec) = (bVar4 & 0x3f) << 8 | 0x200;
                  }
                  *puVar14 = uVar18 >> 4 & 0x1fff | uVar12;
                  FUN_ffff00009078d368(&DAT_ffff00009088a128 + lVar13);
                }
                lVar13 = lVar13 + 0x48;
              } while (lVar13 != 0x90);
              FUN_ffff00009077cea0(0x32);
              uVar10 = 1;
            }
            else {
              uVar10 = 2;
            }
            (*(code *)*puVar22)(puVar22,uVar10);
            FUN_ffff00009077e11c(s__USB__usb_online___d_ffff0000907d5231,DAT_ffff00009088a21d & 1);
          }
          else {
            if (local_68._1_1_ != 5) {
              pcVar9 = s__USB__Unsupported_command_with_R_ffff0000907e877d;
              goto LAB_ffff00009078caec;
            }
            DAT_ffff00009088a21c = (undefined1)(local_68 >> 0x10);
            _DAT_ffff000016701800 = (local_68._2_2_ & 0x7f) << 0x18;
          }
joined_r0xffff00009078cadc:
          if (_DAT_ffff00009088a0c8 == 0) {
            _DAT_ffff000016701100 = _DAT_ffff000016701100 | 0xa0000;
          }
          else if (_DAT_ffff00009088a0c8 == 1) {
            _DAT_ffff000016701100 = _DAT_ffff000016701100 | 0x20000;
          }
          else if (_DAT_ffff00009088a0c8 == 2) {
            _DAT_ffff000016701100 = _DAT_ffff000016701100 | 0x120000;
            FUN_ffff00009078ce2c();
          }
          goto switchD_ffff00009078c664_default;
        }
        FUN_ffff00009077e11c(s__USB__CLEAR_FEATURE_ffff0000907f568a);
        if ((local_68 & 0x1f) == 0) {
          pcVar9 = s__USB__RECIP_DEVICE_ffff0000907f073f;
        }
        else {
          if (((byte)local_68 & 0x1f) == 2) {
            FUN_ffff00009077e11c(s__USB__RECIP_ENDPOINT_ffff0000907e0405);
            uVar19 = local_68 >> 0x20 & 0xf;
            bVar6 = (local_68 & 0x8000000000) != 0;
            puVar2 = (undefined1 *)0xffff000016701200;
            if (bVar6) {
              puVar2 = &DAT_ffff000016701100;
            }
            uVar11 = 0xff9effff;
            if (bVar6) {
              uVar11 = 0xff9fffff;
            }
            puVar14 = (uint *)((ulong)puVar2 | uVar19 << 4);
            puVar3 = &DAT_ffff0000907d038f;
            if (bVar6) {
              puVar3 = &DAT_ffff0000907dbf3a;
            }
            *puVar14 = uVar11 & *puVar14 | 0x400000;
            iVar7 = 0x10000;
            if (!bVar6) {
              iVar7 = 1;
            }
            _DAT_ffff000016701804 =
                 (iVar7 << uVar19 | _DAT_ffff000016701804) & (iVar7 << uVar19 ^ 0xffffffffU);
            FUN_ffff00009077e11c
                      (s__USB__ep_d_s___s_STALL__with_EP_R_ffff0000907ed425,uVar19,puVar3,
                       s_CLEAE_ffff0000907ed44c);
            goto joined_r0xffff00009078cadc;
          }
          if (((byte)local_68 & 0x1f) == 1) {
            pcVar9 = s__USB__RECIP_INTERFACE_ffff0000907f38d4;
          }
          else {
            pcVar9 = s__USB__USB_REQ_CLEAR_FEATURE___OT_ffff0000907dd49d;
          }
        }
LAB_ffff00009078caec:
        FUN_ffff00009077e11c(pcVar9);
      }
    }
LAB_ffff00009078c754:
    _DAT_ffff000016701100 = _DAT_ffff000016701100 & 0xfdbcffff | 0x2020000;
    goto LAB_ffff00009078c8b4;
  }
  switch(_DAT_ffff00009088a0c8) {
  case 0:
    goto code_r0xffff00009078c720;
  case 1:
    plVar20 = (long *)*_DAT_ffff00009088a1b8;
    if ((_DAT_ffff000016701100 >> 0x10 & 1) != 0) {
      if (plVar20 == (long *)0x0) {
        uVar18 = 0;
      }
      else {
        bVar4 = *(byte *)(_DAT_ffff00009088a1b8 + 8);
        uVar12 = _DAT_ffff000016701108;
        if (bVar4 != 0) {
          uVar12 = *(uint *)((ulong)((uint)bVar4 * 0x10 - 0x10) - 0xffffe98fede4) >> 0x10 & 0x7ff;
        }
        uVar18 = (int)plVar20[1] - *(uint *)(plVar20 + 6);
        if (uVar12 <= uVar18) {
          uVar18 = uVar12;
        }
        FUN_ffff00009078cf10(bVar4,*plVar20 + (ulong)*(uint *)(plVar20 + 6),uVar18);
        *(uint *)(plVar20 + 6) = (int)plVar20[6] + uVar18;
      }
      _DAT_ffff000016701100 = uVar11 | 0x10000;
      if ((uVar18 < *(uint *)((long)puVar22 + 0x3c)) || ((int)plVar20[6] == (int)plVar20[1])) {
        _DAT_ffff000016701100 = uVar11 | 0x90000;
      }
    }
    break;
  case 2:
    FUN_ffff00009078ce2c();
    goto switchD_ffff00009078c664_default;
  case 3:
    _DAT_ffff000016701100 = _DAT_ffff000016701100 | 0x80000;
    break;
  default:
    goto switchD_ffff00009078c664_default;
  }
LAB_ffff00009078c8b4:
  _DAT_ffff00009088a0c8 = 0;
switchD_ffff00009078c664_default:
  if ((uVar21 >> 1 & 1) != 0) {
    FUN_ffff00009078d56c();
  }
  return 1;
}


