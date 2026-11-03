
/* RtcCapabilitySwitchUtil::getNetworkTypeFromRaf(int) */

void RtcCapabilitySwitchUtil::getNetworkTypeFromRaf(int param_1)

{
  uint uVar1;
  uint uVar2;
  long lVar3;
  int iVar4;
  uint uVar5;
  uint uVar6;
  undefined4 uVar7;
  ulong uVar8;
  undefined4 uVar9;
  undefined8 local_50;
  long local_48;
  
  lVar3 = tpidr_el0;
  local_48 = *(long *)(lVar3 + 0x28);
  iVar4 = RatConfig_isGsmSupported();
  uVar1 = param_1 & 0xfffefff9;
  if (iVar4 != 0) {
    uVar1 = param_1;
  }
  uVar2 = uVar1;
  if ((uVar1 & 0x10006) != 0) {
    uVar2 = uVar1 | 0x10006;
  }
  if ((uVar2 & 0x8e08) != 0) {
    uVar2 = uVar2 | 0x8e08;
  }
  if ((uVar2 & 0x70) != 0) {
    uVar2 = uVar2 | 0x70;
  }
  if ((uVar2 & 0x3180) != 0) {
    uVar2 = uVar2 | 0x3180;
  }
  if ((uVar2 & 0x84000) != 0) {
    uVar2 = uVar2 | 0x84000;
  }
  if ((int)uVar2 < 0x971f6) {
    if ((int)uVar2 < 0x20000) {
      if ((int)uVar2 < 0x10006) {
        if ((int)uVar2 < 0x31f0) {
          if (uVar2 == 0x70) {
            uVar7 = 5;
            goto LAB_003e472c;
          }
          if (uVar2 == 0x3180) {
            uVar7 = 6;
            goto LAB_003e472c;
          }
        }
        else {
          if (uVar2 == 0x31f0) {
            uVar7 = 4;
            goto LAB_003e472c;
          }
          if (uVar2 == 0x8e08) {
            uVar7 = 2;
            goto LAB_003e472c;
          }
        }
      }
      else if ((int)uVar2 < 0x131f6) {
        if (uVar2 == 0x10006) {
          uVar7 = 1;
          goto LAB_003e472c;
        }
        if (uVar2 == 0x10076) {
          uVar7 = 0x67;
          goto LAB_003e472c;
        }
      }
      else {
        if (uVar2 == 0x131f6) {
          uVar7 = 0x68;
          goto LAB_003e472c;
        }
        if (uVar2 == 0x18e0e) {
          uVar7 = 0;
          goto LAB_003e472c;
        }
        if (uVar2 == 0x1bffe) {
          uVar7 = 7;
          goto LAB_003e472c;
        }
      }
    }
    else if ((int)uVar2 < 0x3bffe) {
      if ((int)uVar2 < 0x30006) {
        if (uVar2 == 0x20000) {
          uVar7 = 0xd;
          goto LAB_003e472c;
        }
        if (uVar2 == 0x28e08) {
          uVar7 = 0xe;
          goto LAB_003e472c;
        }
      }
      else {
        if (uVar2 == 0x30006) {
          uVar7 = 0x10;
          goto LAB_003e472c;
        }
        if (uVar2 == 0x38e0e) {
          uVar7 = 0x12;
          goto LAB_003e472c;
        }
      }
    }
    else if ((int)uVar2 < 0x871f0) {
      if (uVar2 == 0x3bffe) {
        uVar7 = 0x15;
        goto LAB_003e472c;
      }
      if (uVar2 == 0x84000) {
        uVar7 = 0xb;
        goto LAB_003e472c;
      }
    }
    else {
      if (uVar2 == 0x871f0) {
        uVar7 = 8;
        goto LAB_003e472c;
      }
      if (uVar2 == 0x8ce08) {
        uVar7 = 0xc;
        goto LAB_003e472c;
      }
      if (uVar2 == 0x94006) {
        uVar7 = 0x65;
        goto LAB_003e472c;
      }
    }
  }
  else if ((int)uVar2 < 0x184000) {
    if ((int)uVar2 < 0xace08) {
      if ((int)uVar2 < 0x9fffe) {
        if (uVar2 == 0x971f6) {
          uVar7 = 0x69;
          goto LAB_003e472c;
        }
        if (uVar2 == 0x9ce0e) {
          uVar7 = 9;
          goto LAB_003e472c;
        }
      }
      else {
        if (uVar2 == 0x9fffe) {
          uVar7 = 10;
          goto LAB_003e472c;
        }
        if (uVar2 == 0xa4000) {
          uVar7 = 0xf;
          goto LAB_003e472c;
        }
      }
    }
    else if ((int)uVar2 < 0xbce0e) {
      if (uVar2 == 0xace08) {
        uVar7 = 0x13;
        goto LAB_003e472c;
      }
      if (uVar2 == 0xb4006) {
        uVar7 = 0x11;
        goto LAB_003e472c;
      }
    }
    else {
      if (uVar2 == 0xbce0e) {
        uVar7 = 0x14;
        goto LAB_003e472c;
      }
      if (uVar2 == 0xbfffe) {
        uVar7 = 0x16;
        goto LAB_003e472c;
      }
      if (uVar2 == 0x100000) {
        uVar7 = 0x17;
        goto LAB_003e472c;
      }
    }
  }
  else if ((int)uVar2 < 0x1a4000) {
    if ((int)uVar2 < 0x18ce08) {
      if (uVar2 == 0x184000) {
        uVar7 = 0x18;
        goto LAB_003e472c;
      }
      if (uVar2 == 0x1871f0) {
        uVar7 = 0x19;
        goto LAB_003e472c;
      }
    }
    else {
      if (uVar2 == 0x18ce08) {
        uVar7 = 0x1c;
        goto LAB_003e472c;
      }
      if (uVar2 == 0x19ce0e) {
        uVar7 = 0x1a;
        goto LAB_003e472c;
      }
      if (uVar2 == 0x19fffe) {
        uVar7 = 0x1b;
        goto LAB_003e472c;
      }
    }
  }
  else if ((int)uVar2 < 0x1b4006) {
    if (uVar2 == 0x1a4000) {
      uVar7 = 0x1d;
      goto LAB_003e472c;
    }
    if (uVar2 == 0x1ace08) {
      uVar7 = 0x1f;
      goto LAB_003e472c;
    }
  }
  else {
    if (uVar2 == 0x1b4006) {
      uVar7 = 0x1e;
      goto LAB_003e472c;
    }
    if (uVar2 == 0x1bce0e) {
      uVar7 = 0x20;
      goto LAB_003e472c;
    }
    if (uVar2 == 0x1bfffe) {
      uVar7 = 0x21;
      goto LAB_003e472c;
    }
  }
  iVar4 = RatConfig_isC2kSupported();
  uVar5 = RatConfig_isLteFddSupported();
  uVar6 = RatConfig_isLteTddSupported();
  uVar9 = 9;
  if ((uVar6 | uVar5) != 1) {
    uVar9 = 0;
  }
  uVar7 = 4;
  if (iVar4 != 1) {
    uVar7 = uVar9;
  }
  uVar8 = __rfx_is_gt_mode(uVar9);
  if ((uVar8 & 1) == 0) {
    mtkLogI("RtcCapa","getNetworkTypeFromRaf, raf=%d, adj_raf=%d, type=%d",uVar1,uVar2,uVar7);
  }
  else {
    local_50 = 0;
    android::String8::format("%s%s",&local_50,&DAT_001fbebf,"RtcCapa");
    mtkLogI(local_50,"getNetworkTypeFromRaf, raf=%d, adj_raf=%d, type=%d",uVar1,uVar2,uVar7);
    android::String8::~String8((String8 *)&local_50);
  }
LAB_003e472c:
  if (*(long *)(lVar3 + 0x28) != local_48) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail(uVar7);
  }
  return;
}


